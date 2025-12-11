package com.mgmt.CloudKitchen.repo;

import com.mgmt.CloudKitchen.entity.KitchenEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenEventRepository extends JpaRepository<KitchenEvent,Integer> {
}
