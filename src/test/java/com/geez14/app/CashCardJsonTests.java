package com.geez14.app;

import com.geez14.app.entities.CashCard;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

@JsonTest
public class CashCardJsonTests {

    @Autowired
    JacksonTester<CashCard> json;
    @Autowired
    JacksonTester<CashCard[]> jsonList;

    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashCard = new CashCard(99L, 127.45D);

        assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");

        // test weather the "id" value have same value as 99
        assertThat(json.write(cashCard)).extractingJsonPathValue("@.id").isEqualTo(99);

        // test weather the key "amount" exist or not
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");

        // test weather the "amount" value have same value as 127.45
        assertThat(json.write(cashCard)).extractingJsonPathValue("@.amount").isEqualTo(127.45);
    }

    @Test
    void cashCardDeserializationTest() throws IOException {
//        String expected = "{\r\n\"id\": 99,\r\n\"amount\": 127.45\r\n}";
        String expected = """
                {
                    "id": 99,
                    "amount": 127.45
                }
                """;
        assertThat(json.parse(expected)).isEqualTo(new CashCard(99L, 127.45D));
        assertThat(json.parseObject(expected).id()).isEqualTo(99L);
        assertThat(json.parseObject(expected).amount()).isEqualTo(127.45);
    }

    @Test
    void cashCardListSerializationTest() throws IOException {
        CashCard[] cashCards = Arrays.array(
                new CashCard(99L, 123.45),
                new CashCard(100L, 1.00),
                new CashCard(101L, 150.00));
        assertThat(jsonList.write(cashCards)).isEqualToJson("list.json");
    }

    @Test
    void cashCardListDeserializationTest() throws IOException {
        CashCard[] cashCards = Arrays.array(
                new CashCard(99L, 123.45),
                new CashCard(100L, 1.00),
                new CashCard(101L, 150.00));
        String expected =
                """
                                [
                                   {"id": 99, "amount": 123.45},
                                   {"id": 100, "amount": 1.00},
                                   {"id": 101, "amount": 150.00}
                                ]
                        """;
        assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
    }
}
