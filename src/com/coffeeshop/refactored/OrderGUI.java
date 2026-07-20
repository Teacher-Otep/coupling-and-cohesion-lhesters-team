package com.coffeeshop.refactored;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Swing front-end for the coffee shop system.
 * Uses OrderProcessor, DiscountPolicy, TaxCalculator, ReceiptFormatter and
 * OrderRepository exactly as they already exist -- nothing in those classes
 * is modified. All of their System.out.println(...) calls are redirected
 * into the on-screen text area so the receipt and log messages show up
 * in the window instead of the console.
 */
public class OrderGUI extends JFrame {

    private static final Map<String, Double> MENU = new LinkedHashMap<>();
    static {
        MENU.put("Brewed Coffee", 90.0);
        MENU.put("Java Chip Frappe", 150.0);
        MENU.put("Kapeng Barako", 110.0);
        MENU.put("Caramel Macchiato", 165.0);
        MENU.put("Spanish Latte", 140.0);
    }

    private final OrderProcessor processor;

    private final JTextField customerNameField = new JTextField();
    private final JComboBox<String> coffeeTypeCombo = new JComboBox<>(MENU.keySet().toArray(new String[0]));
    private final JTextField priceField = new JTextField();
    private final JCheckBox seniorCheckBox = new JCheckBox("Senior citizen discount (20%, RA 9994)");
    private final JTextArea outputArea = new JTextArea();

    public OrderGUI() {
        super("Coffee Shop Order System");

        // Same wiring as Main.java -- unchanged classes, just constructed here.
        TaxCalculator taxCalculator = new VatTaxCalculator(0.12); // 12% VAT
        ReceiptFormatter receiptFormatter = new PlainTextReceiptFormatter("PHP");
        OrderRepository orderRepository = new FileOrderRepository("orders_log.txt");
        this.processor = new OrderProcessor(taxCalculator, receiptFormatter, orderRepository);

        redirectSystemOutToTextArea();
        buildUI();
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 640);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Customer name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(customerNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Coffee type:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(coffeeTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Price (PHP):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        priceField.setText(String.valueOf(MENU.get(coffeeTypeCombo.getItemAt(0))));
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(seniorCheckBox, gbc);

        JButton placeOrderButton = new JButton("Process Order");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(placeOrderButton, gbc);

        // Auto-fill price when a menu item is picked; still editable by hand.
        coffeeTypeCombo.addActionListener(e -> {
            String selected = (String) coffeeTypeCombo.getSelectedItem();
            priceField.setText(String.valueOf(MENU.get(selected)));
        });

        placeOrderButton.addActionListener(e -> handlePlaceOrder());

        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Receipt / System Log"));

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void handlePlaceOrder() {
        String customerName = customerNameField.getText().trim();
        String coffeeType = (String) coffeeTypeCombo.getSelectedItem();

        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a customer name.",
                    "Missing information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double basePrice;
        try {
            basePrice = Double.parseDouble(priceField.getText().trim());
            if (basePrice <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a positive number.",
                    "Invalid price", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Order order = new Order(customerName, coffeeType, basePrice);
        DiscountPolicy discountPolicy = seniorCheckBox.isSelected()
                ? new SeniorCitizenDiscountPolicy()
                : new NoDiscountPolicy();

        outputArea.append("\n--------------------------------\n");
        processor.processOrder(order, discountPolicy); // unchanged existing logic

        customerNameField.setText("");
        seniorCheckBox.setSelected(false);
    }

    /** Sends everything written to System.out into outputArea instead of the console. */
    private void redirectSystemOutToTextArea() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                SwingUtilities.invokeLater(() -> {
                    outputArea.append(String.valueOf((char) b));
                    outputArea.setCaretPosition(outputArea.getDocument().getLength());
                });
            }
        };
        System.setOut(new PrintStream(out, true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderGUI().setVisible(true));
    }
}
