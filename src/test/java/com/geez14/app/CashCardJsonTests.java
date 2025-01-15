package com.geez14.app;

import com.geez14.app.entities.CashCard;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@JsonTest
public class CashCardJsonTests {

    @Autowired
    JacksonTester<CashCard> json;
    @Autowired
    JacksonTester<CashCard[]> jsonList;

    private CashCard[] cashCards;

    @BeforeEach
    void setUp() {
        cashCards = Arrays.array(new CashCard(99L, 123.45, "Mxtylish"), new CashCard(100L, 1.00, "Mxtylish"), new CashCard(101L, 150.0, "Mxtylish"));
    }

    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashCard = cashCards[0];

        assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");

        // test weather the "id" value have same value as 99
        assertThat(json.write(cashCard)).extractingJsonPathValue("@.id").isEqualTo(99);

        // test weather the key "amount" exist or not
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");

        // test weather the "amount" value have same value as 127.45
        assertThat(json.write(cashCard)).extractingJsonPathValue("@.amount").isEqualTo(123.45);
    }

    @Test
    void cashCardDeserializationTest() throws IOException {
//        String expected = "{\r\n\"id\": 99,\r\n\"amount\": 127.45\r\n,\r\n\"owner\": \"Mxtylish\"}";
        String expected = """
                {
                    "id": 99,
                    "amount": 123.45,
                    "owner": "Mxtylish"
                }
                """;
        assertThat(json.parse(expected)).isEqualTo(cashCards[0]);
        assertThat(json.parseObject(expected).id()).isEqualTo(99L);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
        assertThat(json.parseObject(expected).owner()).isEqualTo("Mxtylish");
    }

    @Test
    void cashCardListSerializationTest() throws IOException {
        assertThat(jsonList.write(cashCards)).isEqualToJson("list.json");
    }

    @Test
    void cashCardListDeserializationTest() throws IOException {
        String expected = """
                        [
                           {"id": 99, "amount": 123.45, "owner": "Mxtylish"},
                           {"id": 100, "amount": 1.00, "owner": "Mxtylish"},
                           {"id": 101, "amount": 150.00, "owner": "Mxtylish"}
                        ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
    }
}
