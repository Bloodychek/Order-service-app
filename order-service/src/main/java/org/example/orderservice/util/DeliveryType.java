package org.example.orderservice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryType {
    PICKUP("самовывоз"),
    DOOR_DELIVERY("доставка до двери");
    private final String name;
}
