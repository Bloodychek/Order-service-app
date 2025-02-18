package org.example.generatenumberservice.repository;

import org.example.generatenumberservice.model.GeneratedNumber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс репозитория для работы с генерируемыми номерами
 */

@Repository
public interface GenerateNumberRepository extends CrudRepository<GeneratedNumber, Long> {
    Optional<GeneratedNumber> findByNumber(String number);
}
