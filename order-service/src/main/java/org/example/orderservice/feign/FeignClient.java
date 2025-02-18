package org.example.orderservice.feign;

import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.cloud.openfeign.FeignClient(name = "generate-number-service", url = "http://localhost:8080",
        configuration = FeignConfig.class)
public interface FeignClient {
    @PostMapping("/numbers")
    String generateOrderNumber();
}
