package org.example.orderservice.repository;

import org.example.orderservice.model.Order;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query("SELECT * FROM public.orders WHERE order_date::DATE = :orderDate AND total_amount > :totalAmount")
    List<Order> getOrderForDateAndGreaterThanTotalOrderAmount(LocalDate orderDate, BigDecimal totalAmount);

    @Query("""
                SELECT * FROM public.orders o
                WHERE o.id NOT IN (
                    SELECT od.order_id FROM public.order_details od WHERE od.product_name = :productName
                )
                AND o.order_date BETWEEN :startDate AND :endDate
            """)
    List<Order> findOrdersNotContainingProductNameInDateRange(
            String productName,
            LocalDate startDate,
            LocalDate endDate
    );
}
