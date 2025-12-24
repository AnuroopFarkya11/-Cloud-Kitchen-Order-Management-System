package com.mgmt.CloudKitchen.repo;

import com.mgmt.CloudKitchen.entity.Customer;
import com.mgmt.CloudKitchen.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
}
