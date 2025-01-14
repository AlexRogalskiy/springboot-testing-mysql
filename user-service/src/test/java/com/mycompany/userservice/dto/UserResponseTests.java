package com.mycompany.userservice.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
class UserResponseTests {

    @Autowired
    private JacksonTester<UserResponse> jacksonTester;

    @Test
    void testSerialize() throws IOException {
        UserResponse userResponse = new UserResponse(1L, "ivan", "ivan@test", LocalDate.parse("2018-01-01"));

        JsonContent<UserResponse> jsonContent = jacksonTester.write(userResponse);

        assertThat(jsonContent)
                .hasJsonPathNumberValue("@.id")
                .extractingJsonPathNumberValue("@.id").isEqualTo(userResponse.getId().intValue());

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.username")
                .extractingJsonPathStringValue("@.username").isEqualTo("ivan");

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.email")
                .extractingJsonPathStringValue("@.email").isEqualTo("ivan@test");

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.birthday")
                .extractingJsonPathStringValue("@.birthday").isEqualTo("2018-01-01");
    }

    @Test
    void testDeserialize() throws IOException {
        String content = "{\"id\":1,\"username\":\"ivan\",\"email\":\"ivan@test\",\"birthday\":\"2018-01-01\"}";

        UserResponse userResponse = jacksonTester.parseObject(content);

        assertThat(userResponse.getId()).isEqualTo(1);
        assertThat(userResponse.getUsername()).isEqualTo("ivan");
        assertThat(userResponse.getEmail()).isEqualTo("ivan@test");
        assertThat(userResponse.getBirthday()).isEqualTo(LocalDate.parse("2018-01-01"));
    }
}