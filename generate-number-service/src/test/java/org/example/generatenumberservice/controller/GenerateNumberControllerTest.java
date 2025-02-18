package org.example.generatenumberservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.generatenumberservice.dto.NumberDto;
import org.example.generatenumberservice.service.GenerateNumberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenerateNumberController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenerateNumberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenerateNumberService generateNumberService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Проверка создания номера")
    void numbersTest() throws Exception {
        // Arrange
        String generatedNumber = "7856420250215";
        NumberDto numberDto = new NumberDto(generatedNumber);

        when(generateNumberService.numbers()).thenReturn(numberDto);

        // Act
        mockMvc.perform(post("/numbers").contentType("application/json")
                        .content(objectMapper.writeValueAsString(numberDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.number").value(generatedNumber));

        // Assert
        verify(generateNumberService, times(1)).numbers();
    }
}