package org.example.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.orderservice.util.DeliveryType;
import org.example.orderservice.util.PaymentType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class Order {
    @Id
    private Long id;

    @Column("order_number")
    private String orderNumber;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("order_date")
    private LocalDate orderDate;

    @Column("recipient")
    private String recipient;

    @Column("delivery_address")
    private String deliveryAddress;

    @Column("payment_type")
    private PaymentType paymentType;

    @Column("delivery_type")
    private DeliveryType deliveryType;

    @MappedCollection(idColumn = "order_id")
    private Set<OrderDetail> orderDetails;
}
