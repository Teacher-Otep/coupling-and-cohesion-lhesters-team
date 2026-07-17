package com.coffeeshop.refactored;

public class NoDiscountPolicy implements DiscountPolicy {
    @Override
    public double applyDiscount(double basePrice) {
        return basePrice;
    }
}