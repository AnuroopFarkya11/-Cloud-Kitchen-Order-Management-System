package com.mgmt.CloudKitchen.repo;

import com.mgmt.CloudKitchen.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser,Integer> {
}
