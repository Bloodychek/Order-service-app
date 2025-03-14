package org.example.orderservice.service;

import org.example.orderservice.model.OrderRequestDto;
import org.example.orderservice.model.OrderResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс для сервиса заказов
 */

public interface OrderService {
    OrderResponseDto findOrderById(Integer id);

    List<OrderResponseDto> getOrderForDateAndGreaterThanTotalOrderAmount(LocalDate date, BigDecimal totalAmount);

    List<OrderResponseDto> findOrdersNotContainingProductNameInDateRange(String productName, LocalDate startDate, LocalDate endDate);

    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
}
