package com.mgmt.CloudKitchen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantity;
    private Double unitPrice;
    private String notes;


    @ManyToOne
    @JoinColumn(name = "menu_item_id",nullable = false)
    @JsonIgnore
    private MenuItems menuItems;


    @ManyToOne
    @JoinColumn(name = "orders",referencedColumnName = "id")
    @JsonIgnore
    private Order order;


}
