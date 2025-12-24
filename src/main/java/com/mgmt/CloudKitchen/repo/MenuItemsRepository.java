package com.mgmt.CloudKitchen.repo;

import com.mgmt.CloudKitchen.entity.MenuItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemsRepository extends JpaRepository<MenuItems,Integer> {
}
