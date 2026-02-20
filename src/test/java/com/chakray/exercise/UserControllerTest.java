package com.chakray.exercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCompleteFlow() throws Exception {
        // 1. Registro: Verificamos que se cree el usuario (201 Created)
        String newUser = "{\"name\":\"Admin\",\"email\":\"admin@mail.com\",\"tax_id\":\"ADMI8203157T1\",\"password\":\"pass123\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUser))
                .andExpect(status().isCreated());

        // 2. Login: Verificamos que el login con RFC y Password cifrada funcione (200 OK)
        String loginData = "{\"username\":\"ADMI8203157T1\",\"password\":\"pass123\"}";

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginData))
                .andExpect(status().isOk());
    }
}
