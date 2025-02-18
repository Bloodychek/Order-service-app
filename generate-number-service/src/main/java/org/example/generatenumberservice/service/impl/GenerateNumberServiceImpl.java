package org.example.generatenumberservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.generatenumberservice.dto.NumberDto;
import org.example.generatenumberservice.mapper.NumberMapper;
import org.example.generatenumberservice.model.GeneratedNumber;
import org.example.generatenumberservice.repository.GenerateNumberRepository;
import org.example.generatenumberservice.service.GenerateNumberService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Реализация сервиса для генерации уникальных номеров
 */

@Service
@RequiredArgsConstructor
public class GenerateNumberServiceImpl implements GenerateNumberService {
    private final GenerateNumberRepository generateNumberRepository;
    private final NumberMapper numberMapper;
    private final Set<String> numbers = new HashSet<>();
    private final Random random = new Random();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public NumberDto numbers() {
        String number;

        while (true) {
            number = generateNumber();
            if (!numbers.contains(number) && generateNumberRepository.findByNumber(number).isEmpty()) {
                break;
            }
        }

        numbers.add(number);

        GeneratedNumber generatedNumber = GeneratedNumber.builder().number(number).build();
        generateNumberRepository.save(generatedNumber);

        return numberMapper.toDto(generatedNumber);
    }

    private String generateNumber() {
        int randomNumber = random.nextInt(100000);
        String date = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        return String.format("%05d%s", randomNumber, date);
    }
}