package org.example.orderservice.feign;

import org.springframework.web.bind.annotation.PostMapping;

/**
 * Интерфейс Feign-клиента для работы с сервисом генерации номеров заказов
 */

@org.springframework.cloud.openfeign.FeignClient(name = "generate-number-service",
        url = "http://generate-number-service:8080",
        configuration = FeignConfig.class)
public interface FeignClient {
    @PostMapping("/numbers")
    String generateOrderNumber();
}
