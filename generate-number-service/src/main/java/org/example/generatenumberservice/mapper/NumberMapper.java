package org.example.generatenumberservice.mapper;

import org.example.generatenumberservice.dto.NumberDto;
import org.example.generatenumberservice.model.GeneratedNumber;
import org.mapstruct.Mapper;

/**
 * Интерфейс для преобразования объектов NumberEntity и NumberDto
 */

@Mapper(componentModel = "spring")
public interface NumberMapper {
    NumberDto toDto(GeneratedNumber generatedNumber);
}
