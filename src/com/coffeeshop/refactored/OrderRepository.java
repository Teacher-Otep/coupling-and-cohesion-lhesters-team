package com.coffeeshop.refactored;

public interface OrderRepository {
    void save(Order order, double finalPrice);
}