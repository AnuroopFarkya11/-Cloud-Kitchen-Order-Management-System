package com.mgmt.CloudKitchen.dto;


import com.mgmt.CloudKitchen.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * {
 *   "customerId": 1,
 *   "items": [
 *     {
 *       "menuItemId": 1,
 *       "quantity": 2,
 *       "notes": "Please make it extra spicy"
 *     },
 *     {
 *       "menuItemId": 3,
 *       "quantity": 1,
 *       "notes": null
 *     }
 *   ]
 * }
 */
@Data
@AllArgsConstructor
public class OrderItemReqDTO {
    private Integer menuItemId;
    private Integer quantity;
    private String notes = null;
}
