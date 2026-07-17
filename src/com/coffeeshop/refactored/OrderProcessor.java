package com.coffeeshop.refactored;

public class OrderProcessor {
    private final TaxCalculator taxCalculator;
    private final ReceiptFormatter receiptFormatter;
    private final OrderRepository orderRepository;

    public OrderProcessor(TaxCalculator taxCalculator,
                           ReceiptFormatter receiptFormatter,
                           OrderRepository orderRepository) {
        this.taxCalculator = taxCalculator;
        this.receiptFormatter = receiptFormatter;
        this.orderRepository = orderRepository;
    }

    public void processOrder(Order order, DiscountPolicy discountPolicy) {
        System.out.println("[System] Calculating final totals...");

        double subtotal = order.getBasePrice();
        double discountedPrice = discountPolicy.applyDiscount(subtotal);
        double discountAmount = subtotal - discountedPrice;
        double finalPrice = taxCalculator.calculateFinalPrice(discountedPrice);

        String receipt = receiptFormatter.format(order, subtotal, discountAmount, finalPrice);
        System.out.println(receipt);

        orderRepository.save(order, finalPrice);
    }
}