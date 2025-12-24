package com.mgmt.CloudKitchen.repo;

import com.mgmt.CloudKitchen.entity.Order;
import com.mgmt.CloudKitchen.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findByIdWithOrderItems(@Param("id") Integer id);

    List<Order> findByStatus(OrderStatus status);
}
