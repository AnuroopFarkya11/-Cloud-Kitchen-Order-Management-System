package com.mgmt.CloudKitchen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KitchenEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private KitchenEventStatus status = KitchenEventStatus.NEW;

    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "orders_id",nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = true)
    @JsonIgnore
    private AppUser staff;

}
