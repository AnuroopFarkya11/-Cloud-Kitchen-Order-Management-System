package com.mgmt.CloudKitchen.repo;

import com.mgmt.CloudKitchen.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
}
