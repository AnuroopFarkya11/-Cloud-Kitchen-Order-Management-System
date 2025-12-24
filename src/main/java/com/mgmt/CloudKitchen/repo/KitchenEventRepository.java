package com.mgmt.CloudKitchen.repo;

import com.mgmt.CloudKitchen.entity.KitchenEvent;
import com.mgmt.CloudKitchen.entity.KitchenEventStatus;
import com.mgmt.CloudKitchen.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KitchenEventRepository extends JpaRepository<KitchenEvent,Integer> {
    List<KitchenEvent> findByStatus(KitchenEventStatus status);
    Optional<KitchenEvent> findByOrder_Id(Integer orderId);
}
