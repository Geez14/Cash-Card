package com.geez14.app;

import com.geez14.app.entities.CashCard;
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

    @Test
    void cashCardSerializationTest()throws IOException {
        CashCard cashCard = new CashCard(99, 127.45);

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
        assertThat(json.parse(expected)).isEqualTo(new CashCard(99, 127.45));
        assertThat(json.parseObject(expected).id()).isEqualTo(99L);
        assertThat(json.parseObject(expected).amount()).isEqualTo(127.45);
    }
}
