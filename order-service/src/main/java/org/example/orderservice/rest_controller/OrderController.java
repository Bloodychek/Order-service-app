package org.example.orderservice.rest_controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.api.OrdersApi;
import org.example.orderservice.model.OrderRequestDto;
import org.example.orderservice.model.OrderResponseDto;
import org.example.orderservice.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Класс-контроллер для работы с заказами
 */

@RestController
@RequiredArgsConstructor
public class OrderController implements OrdersApi {
    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequestDto));
    }

    @Override
    public ResponseEntity<OrderResponseDto> getOrderById(Integer id) {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    @Override
    public ResponseEntity<List<OrderResponseDto>> getOrdersByDateAndAmount(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, BigDecimal totalAmount) {
        return ResponseEntity.ok(orderService.getOrderForDateAndGreaterThanTotalOrderAmount(date, totalAmount));
    }

    @Override
    public ResponseEntity<List<OrderResponseDto>> getOrdersByFilter(String productName, LocalDate startDate, LocalDate endDate) {
        return ResponseEntity.ok(orderService.findOrdersNotContainingProductNameInDateRange(productName, startDate, endDate));
    }
}
