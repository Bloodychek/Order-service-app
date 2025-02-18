package org.example.orderservice.mapper;

import org.example.orderservice.model.Order;
import org.example.orderservice.model.OrderDetail;
import org.example.orderservice.model.OrderRequestDto;
import org.example.orderservice.model.OrderResponseDto;
import org.example.orderservice.model.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "products", source = "orderDetails")
    OrderResponseDto toDto(Order order);

    @Mapping(target = "orderDetails", source = "products")
    Order toEntity(OrderRequestDto orderRequestDto);

    @Mapping(target = "unitPrice", source = "unitPrice")
    OrderDetail toOrderDetail(ProductDto productDto);

    @Mapping(target = "unitPrice", source = "unitPrice")
    ProductDto toProductDto(OrderDetail orderDetail);

    List<OrderDetail> toOrderDetailList(List<ProductDto> products);

    List<ProductDto> toProductDtoList(List<OrderDetail> orderDetails);
}
