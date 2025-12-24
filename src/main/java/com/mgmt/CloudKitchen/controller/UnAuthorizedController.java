package com.mgmt.CloudKitchen.controller;


import com.mgmt.CloudKitchen.config.JwtUtil;
import com.mgmt.CloudKitchen.entity.MenuItems;
import com.mgmt.CloudKitchen.entity.Order;
import com.mgmt.CloudKitchen.handler.CustomException;
import com.mgmt.CloudKitchen.repo.MenuItemsRepository;
import com.mgmt.CloudKitchen.repo.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UnAuthorizedController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private MenuItemsRepository menuItemsRepository;

    @Autowired
    private OrderRepository orderRepository;




    @GetMapping("/api/v1/menu")
    public ResponseEntity<List<MenuItems>> getMenuItems() {
        List<MenuItems> menuItemsList = menuItemsRepository.findAll();
        return ResponseEntity.ok(menuItemsList);
    }

    @GetMapping("/api/v1/orders/{id}/status")
    public ResponseEntity<Object> getOrderDetails(@PathVariable Integer id) {
        log.info("getOrderDetails invoked");

        if (id != null && id > 0) {
            log.info("Order ID is valid");
            Order order = orderRepository.findById(id).orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
            return new ResponseEntity<>(order.getStatus(), HttpStatus.OK);
        }
        throw new CustomException("Order ID is null or invalid", HttpStatus.BAD_REQUEST);
    }


}
