package com.mgmt.CloudKitchen.controller;

import com.mgmt.CloudKitchen.dto.OrderReqDTO;
import com.mgmt.CloudKitchen.entity.Customer;
import com.mgmt.CloudKitchen.entity.DeliveryPartner;
import com.mgmt.CloudKitchen.entity.KitchenEvent;
import com.mgmt.CloudKitchen.entity.KitchenEventStatus;
import com.mgmt.CloudKitchen.entity.MenuItems;
import com.mgmt.CloudKitchen.entity.Order;
import com.mgmt.CloudKitchen.entity.OrderItem;
import com.mgmt.CloudKitchen.entity.OrderStatus;
import com.mgmt.CloudKitchen.handler.CustomException;
import com.mgmt.CloudKitchen.repo.CustomerRepository;
import com.mgmt.CloudKitchen.repo.DeliveryPartnerRepository;
import com.mgmt.CloudKitchen.repo.KitchenEventRepository;
import com.mgmt.CloudKitchen.repo.MenuItemsRepository;
import com.mgmt.CloudKitchen.repo.OrderItemRepository;
import com.mgmt.CloudKitchen.repo.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class AuthorizedController {


    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    @Autowired
    private KitchenEventRepository kitchenEventRepository;
    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;


    //    {
//        "customerId": 1,
//            "items": [
//        {
//            "menuItemId": 1,
//                "quantity": 2,
//                "notes": "Please make it extra spicy"
//        },
//        {
//            "menuItemId": 3,
//                "quantity": 1,
//                "notes": null
//        }
//  ]
//    }
    @PostMapping("/api/v1/orders")
//    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Object> placeOrder(@RequestBody OrderReqDTO orderDto) {
        Customer customer = null;
        if (orderDto.getCustomerId() != null) {
            customer = customerRepository.findById(orderDto.getCustomerId()).orElseThrow(() -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
        } else {
            throw new CustomException("Customer id is null", HttpStatus.BAD_REQUEST);
        }

        Set<OrderItem> orderItemSet = null;
        if (orderDto.getItems() != null) {
            orderItemSet = orderDto.getItems().stream().map(orderItemReqDTO -> {
                MenuItems menuItem = menuItemsRepository.findById(orderItemReqDTO.getMenuItemId()).orElseThrow(() -> new CustomException("Menu ID is not present in DB", HttpStatus.BAD_REQUEST));
                if (menuItem.isAvailable()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(orderItemReqDTO.getQuantity());
                    orderItem.setNotes(orderItemReqDTO.getNotes());
                    orderItem.setMenuItems(menuItem);
                    orderItem.setUnitPrice(menuItem.getPrice());
                    return orderItem;
                }
                throw new CustomException("Menu item is not available : " + menuItem.getName(), 200);
            }).collect(Collectors.toSet());
        } else {
            throw new CustomException("Order list is empty", HttpStatus.BAD_REQUEST);
        }


        Double totalAmount = 0.0;

        if (orderItemSet != null) {
            for (var orderItem : orderItemSet) {
                totalAmount += (orderItem.getUnitPrice() * orderItem.getQuantity());
            }
        }

        if (customer != null && orderItemSet != null && totalAmount > 0) {


            Order order = new Order();
            order.setOrderDate(new Date());
            order.setTotalAmount(totalAmount);
            order.setStatus(OrderStatus.NEW);
            order.setCustomer(customer);
            order.setOrderItems(orderItemSet);
            for (var orderItem : orderItemSet) {
                orderItem.setOrder(order);
            }

            KitchenEvent kitchenEvent = new KitchenEvent();
            kitchenEvent.setStatus(KitchenEventStatus.NEW);
            kitchenEvent.setOrder(order);
            kitchenEvent.setTimestamp(new Timestamp(System.currentTimeMillis()));

            order.getKitchenEvents().add(kitchenEvent);
            order.setKitchenEvents(order.getKitchenEvents());


            Order savedOrder = orderRepository.save(order);

            log.info("Order item after saving the product " + savedOrder.getOrderItems().size());

            Order saved2 = orderRepository.findById(savedOrder.getId()).orElse(null);

            log.info("order fetching again :  " + saved2.getOrderItems().size());


            Map<String, Object> response = new HashMap<>();
            response.put("orderId", savedOrder.getId());
            response.put("status", savedOrder.getStatus());

            return new ResponseEntity<>(saved2, HttpStatus.ACCEPTED);
        }

        throw new CustomException("Something went wrong while creating order", 500);

    }


    @GetMapping("/api/v1/orders/{id}")
    @Transactional
    public ResponseEntity<Object> getOrderDetails(@PathVariable Integer id) {
        log.info("getOrderDetails invoked");
        if (id != null && id > 0) {
            log.info("Order ID is valid");
            Order order = orderRepository.findByIdWithOrderItems(id).orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));

            order.setTotalAmount((double) Math.round(order.getTotalAmount()));
            log.info("Order found with itmes " + order.getOrderItems().size());
            return new ResponseEntity<>(order, HttpStatus.OK);

        }
        throw new CustomException("Order ID is null or invalid", HttpStatus.BAD_REQUEST);

    }


    @GetMapping("/api/v1/kitchen/orders/new")
    public ResponseEntity<Object> getAllNewOrder() {
        log.info("getAllNewOrder invoked");
        try {
            List<KitchenEvent> kitchenEvents = kitchenEventRepository.findByStatus(KitchenEventStatus.NEW);
            return new ResponseEntity<>(kitchenEvents, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new CustomException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/api/v1/kitchen/orders/{id}/status")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable Integer id, @RequestBody Map<String, String> statusBody) {
        log.info("updateOrderStatus invoked");
        String status = statusBody.get("status");
        if ((id != null && id > 0) || status.isEmpty()) {

            log.info("Order ID is valid for updating the status from kitchen");
            Order order = orderRepository.findById(id).orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
            KitchenEvent kitchenEvent = kitchenEventRepository.findByOrder_Id(id).orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
            log.info("Order found");
            KitchenEventStatus kitchenEventStatus = null;
            OrderStatus orderStatus = null;
            try {
                kitchenEventStatus = KitchenEventStatus.valueOf(status);
                orderStatus = OrderStatus.valueOf(status);
            } catch (IllegalArgumentException exception) {
                log.error(exception.getMessage(), exception);
                throw new CustomException("Invalid status", HttpStatus.BAD_REQUEST);
            }

            kitchenEvent.setStatus(kitchenEventStatus);
            order.setStatus(orderStatus);
            orderRepository.save(order);
            kitchenEventRepository.save(kitchenEvent);
            return new ResponseEntity<>("Updated the order status", HttpStatus.OK);

        }
        throw new CustomException("Order ID is null or invalid | status is invalid", HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/api/v1/delivery/orders/ready")
    public ResponseEntity<Object> getReadyOrders() {
        log.info("getReadyOrders invoked");
        try {
            List<Order> orders = orderRepository.findByStatus(OrderStatus.READY);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage(), 500);
        }
    }


    @PutMapping("/api/v1/delivery/orders/{id}/assign")
    public ResponseEntity<Object> assignOrder(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        log.info("assignOrder invoked");
        Integer deliveryPartnerId = body.get("deliveryPartnerId");
        if ((id != null && id > 0) || (deliveryPartnerId !=null && deliveryPartnerId>0)) {




                Order order = orderRepository.findById(id).orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));

                DeliveryPartner deliveryPartner = deliveryPartnerRepository.findById(deliveryPartnerId).orElseThrow(() -> new CustomException("Delivery Partner not found", HttpStatus.NOT_FOUND));

                order.setStatus(OrderStatus.PICKED_UP);
                orderRepository.save(order);

                Map<String, Object> res = Map.of("orderId",order.getId(),"status",order.getStatus());
                return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new CustomException("Order/Delivery ID is null or invalid",400);

    }


}
