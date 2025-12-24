package com.mgmt.CloudKitchen.repo;

import com.mgmt.CloudKitchen.entity.AppUser;
import com.mgmt.CloudKitchen.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

    Optional<RefreshToken> findByToken(String token);
    @Modifying
    int deleteByUser(AppUser user);

}
