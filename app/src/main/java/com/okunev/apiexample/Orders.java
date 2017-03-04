package com.okunev.apiexample;

import java.util.List;

/**
 * Project ApiExample. Created by gwa on 3/4/17.
 */

public class Orders {
    List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public static class Order {
        String name;
        Long id;

        public String getName() {
            return name;
        }

        public Long getId() {
            return id;
        }
    }

}
