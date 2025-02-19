package org.example.orderservice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс-enum для определения способа оплаты
 */

@AllArgsConstructor
@Getter
public enum PaymentType {
    CARD("карта"),
    CASH("наличные");
    private final String name;
}
