package org.example.generatenumberservice.service.impl;

import org.example.generatenumberservice.dto.NumberDto;
import org.example.generatenumberservice.mapper.NumberMapper;
import org.example.generatenumberservice.model.GeneratedNumber;
import org.example.generatenumberservice.repository.GenerateNumberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateNumberServiceImplTest {

    @InjectMocks
    private GenerateNumberServiceImpl generateNumberService;

    @Mock
    private GenerateNumberRepository generateNumberRepository;

    @Mock
    private NumberMapper numberMapper;

    @Test
    @DisplayName("Проверка создания номера")
    void numbers_shouldGenerateDifferentNumberWhenAlreadyExists() {
        // Arrange
        NumberDto numberDto;
        numberDto = NumberDto.builder().number("1234520250216").build();

        when(generateNumberRepository.findByNumber(anyString())).thenReturn(Optional.empty());
        when(numberMapper.toDto(any(GeneratedNumber.class))).thenReturn(numberDto);

        // Act
        NumberDto result = generateNumberService.numbers();

        // Assert
        verify(generateNumberRepository, times(1)).findByNumber(anyString());
        verify(generateNumberRepository, times(1)).save(any(GeneratedNumber.class));
        assertEquals(numberDto.getNumber(), result.getNumber());
    }
}