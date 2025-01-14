package com.geez14.app;

//import com.fasterxml.jackson.databind.ObjectMapper;

import com.geez14.app.entities.CashCard;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;

/**
 * Test cas will run in alphanumeric order of method names
 */

// resets the database after each method run, not recommended, instead use method level annotation!
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashCardApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnACashCardWhenDataIsSaved() {
        // getForEntity is Get Request on "uri" and get object CashCard.class
        ResponseEntity<CashCard> responseEntity = restTemplate.withBasicAuth("Mxtylish", "password1234").getForEntity("/cashcards/99", CashCard.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // ObjectMapper mapper = new ObjectMapper();
        // String jsonResponse = mapper.writeValueAsString(responseEntity.getBody());
        // DocumentContext documentContext = JsonPath.parse(jsonResponse);
        // Number id = documentContext.read("$.id");

        CashCard cashCard = responseEntity.getBody();
        Assertions.assertThat(cashCard).isNotNull();
        Assertions.assertThat(cashCard.id()).isEqualTo(99);
        Assertions.assertThat(cashCard.amount()).isEqualTo(123.45);
    }

    @Test
    @DirtiesContext
    void shouldCreateANewCashCard() {
        CashCard cashCard = new CashCard(null, 128.034, null);
        // in post request we send URI thing
        ResponseEntity<Void> responseEntity = restTemplate.withBasicAuth("Mxtylish", "password1234").postForEntity("/cashcards", cashCard, Void.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        // Assertion to verify the resource creation
        URI locatedResource = responseEntity.getHeaders().getLocation();
        ResponseEntity<String> check = restTemplate.withBasicAuth("Mxtylish", "password1234").getForEntity(locatedResource, String.class);

        Assertions.assertThat(check.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(check.getBody());
        // Assertion to check body
        Assertions.assertThat(check.getBody()).isNotNull();
        Number id = documentContext.read("$.id");
        Double amount = documentContext.read("$.amount");

        Assertions.assertThat(id).isNotNull();
        Assertions.assertThat(amount).isEqualTo(128.034);
    }

    @Test
    void shouldNotReturnCashCardWithUnknownId() {
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("Mxtylish", "password1234").getForEntity("/cashcards/1000", String.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(responseEntity.getBody()).isBlank();
    }


    @Test
    void shouldReturnAllCashCardsWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("Mxtylish", "password1234")
                .getForEntity("/cashcards", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int cashCardCount = documentContext.read("$.length()");
        Assertions.assertThat(cashCardCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        Assertions.assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);

        JSONArray amounts = documentContext.read("$..amount");
        Assertions.assertThat(amounts).containsExactlyInAnyOrder(123.45, 1.00, 150.00);
    }

    /**
     * [
     * {CASH-CARD-OBJECT: A},
     * {CASH-CARD-OBJECT: B},
     * ]
     */
    @Test
    void shouldReturnAPageOfCashCard() {
        // Json String
        ResponseEntity<String> response = restTemplate.withBasicAuth("Mxtylish", "password1234").getForEntity("/cashcards?page=0?size=3", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        Assertions.assertThat(page.size()).isEqualTo(3);
    }

    @Test
    void shouldReturnASortedPageOfCashCards() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("Mxtylish", "password1234").getForEntity("/cashcards?page=0&size=1&sort=amount,desc", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        Assertions.assertThat(page.size()).isEqualTo(1);

        double amount = documentContext.read("$[0].amount");
        Assertions.assertThat(amount).isEqualTo(150.00);
    }

    @Test
    void shouldReturnASortedPageOfCashCardWithNoParametersAndUseDefaultValues() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("Mxtylish", "password1234").getForEntity("/cashcards", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        Assertions.assertThat(page.size()).isEqualTo(3);

        JSONArray amount = documentContext.read("$..amount");
        Assertions.assertThat(amount).containsExactly(1.00, 123.45, 150.00);
    }

    @Test
    void shouldNotReturnACashCardWhenUsingBadCredentials() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("BAD-GUY", "password1234").getForEntity("/cashcards/99", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        response = restTemplate.withBasicAuth("Mxtylish", "WRONG-PASSWORD").getForEntity("/cashcards/99", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotAllowAccessToCashCardsTheyDoNotOwn() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("Mxtylish", "password1234").getForEntity("/cashcards/102", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
