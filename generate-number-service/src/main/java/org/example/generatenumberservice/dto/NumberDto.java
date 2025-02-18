package org.example.generatenumberservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс-DTO для сущности NumberEntity
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NumberDto {
    private String number;
}
