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

    public void processOrder(Order order) {
        System.out.println("[System] Calculating final totals...");
        double finalPrice = taxCalculator.calculateFinalPrice(order.getBasePrice());

        String receipt = receiptFormatter.format(order, finalPrice);
        System.out.println(receipt);

        orderRepository.save(order, finalPrice);
    }
}