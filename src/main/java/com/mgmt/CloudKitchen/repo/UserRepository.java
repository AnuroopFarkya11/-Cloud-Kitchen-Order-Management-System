package com.mgmt.CloudKitchen.repo;

import com.mgmt.CloudKitchen.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser,Integer> {
    Optional<AppUser> findByUsername(String username);
}
