package com.mgmt.CloudKitchen.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String vehicleType;
    private String currentLocation;
    private Boolean isAvailable;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",unique = true,nullable = false)
    private AppUser user;

    @OneToMany(mappedBy = "deliveryPartner")
    private Set<Order> orders;

}
