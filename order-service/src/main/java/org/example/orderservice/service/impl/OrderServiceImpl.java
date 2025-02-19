package org.example.orderservice.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.feign.FeignClient;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.model.Order;
import org.example.orderservice.model.OrderDetail;
import org.example.orderservice.model.OrderRequestDto;
import org.example.orderservice.model.OrderResponseDto;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса заказа
 */

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final FeignClient feignClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;

    @Override
    public OrderResponseDto findOrderById(Integer id) {
        Order order = orderRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> getOrderForDateAndGreaterThanTotalOrderAmount(LocalDate date, BigDecimal totalAmount) {
        List<Order> orders = orderRepository.getOrderForDateAndGreaterThanTotalOrderAmount(date, totalAmount);

        if (orders.isEmpty()) {
            throw new RuntimeException("Заказы на указанную дату и сумму не найдены");
        }

        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> findOrdersNotContainingProductNameInDateRange(String productName, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findOrdersNotContainingProductNameInDateRange(productName, startDate, endDate);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        String orderNumber = feignClient.generateOrderNumber();

        try {
            JsonNode jsonNode = objectMapper.readTree(orderNumber);
            orderNumber = jsonNode.get("number").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка парсинга orderNumber", e);
        }

        Order order = orderMapper.toEntity(orderRequestDto);
        order.setOrderNumber(orderNumber);
        order.setOrderDate(LocalDate.now());

        BigDecimal totalAmount = order.getOrderDetails().stream()
                .map(OrderDetail::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

}