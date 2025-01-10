package com.geez14.app;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.geez14.app.entities.CashCard;
//import com.jayway.jsonpath.DocumentContext;
//import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashCardApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnACashCardWhenDataIsSaved() throws JsonProcessingException {
        ResponseEntity<CashCard> responseEntity = restTemplate.getForEntity("/cashcards/99", CashCard.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

//        ObjectMapper mapper = new ObjectMapper();
//        String jsonResponse = mapper.writeValueAsString(responseEntity.getBody());
//        DocumentContext documentContext = JsonPath.parse(jsonResponse);
//        Number id = documentContext.read("$.id");

        CashCard cashCard = responseEntity.getBody();
        Assertions.assertThat(cashCard).isNotNull();
        Assertions.assertThat(cashCard.id()).isEqualTo(99);

        Assertions.assertThat(cashCard.amount()).isEqualTo(123.45);
    }

    @Test
    void shouldNotReturnAnyThing() {
        ResponseEntity<CashCard> responseEntity = restTemplate.getForEntity("/cashcards", CashCard.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
    }
}
