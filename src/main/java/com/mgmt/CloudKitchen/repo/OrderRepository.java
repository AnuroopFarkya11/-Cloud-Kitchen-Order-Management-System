package com.mgmt.CloudKitchen.repo;

import com.mgmt.CloudKitchen.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {
}
