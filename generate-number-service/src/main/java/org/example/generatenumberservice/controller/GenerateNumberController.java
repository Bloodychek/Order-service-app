package org.example.generatenumberservice.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.generatenumberservice.dto.NumberDto;
import org.example.generatenumberservice.service.GenerateNumberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс-Контроллер для работы с генерируемыми номерами
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/numbers")
@Tag(name = "GenerateNumberController", description = "Контроллер для работы с генерируемыми номерами")
public class GenerateNumberController {
    private final GenerateNumberService generateNumberService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешное создание номера"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "404", description = "Запрос не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")

    })
    @PostMapping
    public ResponseEntity<NumberDto> numbers() {
        return ResponseEntity.status(HttpStatus.CREATED).body(generateNumberService.numbers());
    }
}
