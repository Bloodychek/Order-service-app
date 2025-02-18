package org.example.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orderservice.feign.FeignClient;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.model.Order;
import org.example.orderservice.model.OrderDetail;
import org.example.orderservice.model.OrderRequestDto;
import org.example.orderservice.model.OrderResponseDto;
import org.example.orderservice.model.ProductDto;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.util.DeliveryType;
import org.example.orderservice.util.PaymentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private FeignClient feignClient;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequestDto orderRequestDto;
    private Order order;
    private OrderResponseDto orderResponseDto;

    @BeforeEach
    void setUp() {
        ProductDto productDto = new ProductDto()
                .productSku(123)
                .productName("Ноутбук")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(999.99));

        orderRequestDto = new OrderRequestDto()
                .recipient("Иван Иванов")
                .deliveryAddress("ул. Ленина, д. 10")
                .paymentType(OrderRequestDto.PaymentTypeEnum.CARD)
                .deliveryType(OrderRequestDto.DeliveryTypeEnum.DOOR_DELIVERY)
                .products(Collections.singletonList(productDto));

        order = new Order();
        order.setId(1L);
        order.setOrderNumber("123456");
        order.setTotalAmount(BigDecimal.valueOf(1999.98));
        order.setOrderDate(LocalDate.now());
        order.setRecipient("Иван Иванов");
        order.setDeliveryAddress("ул. Ленина, д. 10");
        order.setPaymentType(PaymentType.CARD);
        order.setDeliveryType(DeliveryType.DOOR_DELIVERY);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(1L);
        orderDetail.setProductSku(123L);
        orderDetail.setProductName("Ноутбук");
        orderDetail.setQuantity(2);
        orderDetail.setUnitPrice(BigDecimal.valueOf(999.99));
        orderDetail.setOrderId(1L);

        order.setOrderDetails(Collections.singleton(orderDetail));

        orderResponseDto = new OrderResponseDto()
                .id(1)
                .orderNumber("12345")
                .totalAmount(BigDecimal.valueOf(1999.98))
                .orderDate(LocalDate.now())
                .recipient("Иван Иванов")
                .deliveryAddress("ул. Ленина, д. 10")
                .paymentType("CARD")
                .deliveryType("DOOR_DELIVERY")
                .products(Collections.singletonList(productDto));
    }

    @Test
    @DisplayName("Должен вернуть заказ по ID")
    void findOrderById() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // Act
        OrderResponseDto result = orderService.findOrderById(1);

        // Assert
        assertNotNull(result);
        assertEquals("12345", result.getOrderNumber());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    @DisplayName("Должен вернуть заказы по дате и минимальной сумме")
    void getOrderForDateAndGreaterThanTotalOrderAmount() {
        // Arrange
        LocalDate date = LocalDate.now();
        BigDecimal totalAmount = BigDecimal.valueOf(50);

        when(orderRepository.getOrderForDateAndGreaterThanTotalOrderAmount(date, totalAmount))
                .thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // Act
        List<OrderResponseDto> result = orderService.getOrderForDateAndGreaterThanTotalOrderAmount(date, totalAmount);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).getOrderForDateAndGreaterThanTotalOrderAmount(date, totalAmount);
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    @DisplayName("Должен вернуть заказы, не содержащие определенный продукт в заданном диапазоне дат")
    void findOrdersNotContainingProductNameInDateRangeTest() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        String productName = "ProductX";

        when(orderRepository.findOrdersNotContainingProductNameInDateRange(productName, startDate, endDate))
                .thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // Act
        List<OrderResponseDto> result = orderService.findOrdersNotContainingProductNameInDateRange(productName, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findOrdersNotContainingProductNameInDateRange(productName, startDate, endDate);
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    @DisplayName("Должен создавать заказ")
    void createOrderTest() throws JsonProcessingException {
        // Arrange
        when(feignClient.generateOrderNumber()).thenReturn("{\"number\": \"12345\"}");
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree("{\"number\": \"12345\"}")).thenReturn(jsonNode);
        when(jsonNode.get("number")).thenReturn(jsonNode);
        when(orderMapper.toEntity(orderRequestDto)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // Act
        OrderResponseDto result = orderService.createOrder(orderRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("12345", result.getOrderNumber());
        assertEquals(BigDecimal.valueOf(1999.98), result.getTotalAmount());
        assertEquals("Иван Иванов", result.getRecipient());
        assertEquals("ул. Ленина, д. 10", result.getDeliveryAddress());
        verify(feignClient, times(1)).generateOrderNumber();
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderMapper, times(1)).toDto(order);
    }
}
