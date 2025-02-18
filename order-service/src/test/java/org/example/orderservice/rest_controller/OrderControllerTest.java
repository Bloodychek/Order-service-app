package org.example.orderservice.rest_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orderservice.model.OrderRequestDto;
import org.example.orderservice.model.OrderResponseDto;
import org.example.orderservice.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Должен создавать новый заказ")
    void createOrderTest() throws Exception {
        // Arrange
        OrderRequestDto request = new OrderRequestDto();
        request.setRecipient("Иван Иванов");
        request.setDeliveryAddress("ул. Ленина, д. 10, кв. 5, Москва");

        OrderResponseDto response = new OrderResponseDto();
        when(orderService.createOrder(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    @DisplayName("Должен возвращать заказ по ID")
    void getOrderByIdTest() throws Exception {
        // Arrange
        Integer id = 1;
        when(orderService.findOrderById(id)).thenReturn(new OrderResponseDto());

        // Act & Assert
        mockMvc.perform(get("/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService, times(1)).findOrderById(id);
    }

    @Test
    @DisplayName("Должен возвращать заказы по дате и минимальной сумме")
    void getOrdersByDateAndAmountTest() throws Exception {
        // Arrange
        LocalDate date = LocalDate.of(2024, 2, 1);
        BigDecimal totalAmount = BigDecimal.valueOf(100.0);

        when(orderService.getOrderForDateAndGreaterThanTotalOrderAmount(date, totalAmount)).thenReturn(List.of(new OrderResponseDto()));

        // Act & Assert
        mockMvc.perform(get("/orders/search")
                        .param("date", date.toString())
                        .param("totalAmount", totalAmount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService, times(1)).getOrderForDateAndGreaterThanTotalOrderAmount(date, totalAmount);
    }

    @Test
    @DisplayName("Должен искать заказы по фильтру")
    void getOrdersByFilterTest() throws Exception {
        // Arrange
        String productName = "Ноутбук";
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 2, 1);

        when(orderService.findOrdersNotContainingProductNameInDateRange(productName, startDate, endDate))
                .thenReturn(List.of(new OrderResponseDto()));

        // Act & Assert
        mockMvc.perform(get("/orders/filter")
                        .param("productName", productName)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService, times(1))
                .findOrdersNotContainingProductNameInDateRange(productName, startDate, endDate);
    }
}
