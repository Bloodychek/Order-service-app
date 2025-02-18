package org.example.generatenumberservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Класс-сущность для хранения генерируемых номеров
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("generated_numbers")
public class GeneratedNumber {
    @Id
    @Column("id")
    private Long id;

    @Column("number")
    private String number;
}
