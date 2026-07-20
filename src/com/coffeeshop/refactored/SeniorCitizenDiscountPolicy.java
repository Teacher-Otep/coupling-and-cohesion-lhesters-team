package com.coffeeshop.refactored;

public class SeniorCitizenDiscountPolicy implements DiscountPolicy {
    private static final double DISCOUNT_RATE = 0.20; // 20% under RA 9994

    @Override
    public double applyDiscount(double basePrice) {
        return basePrice - (basePrice * DISCOUNT_RATE);
    }
}