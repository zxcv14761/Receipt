package com.example.demo.service;

import com.example.demo.repository.TaxConfigRepository;
import com.example.demo.controller.req.PrintReq;
import com.example.demo.entity.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ReceiptService {

    public void printReceipt(PrintReq printReq) {
        double subtotal = 0.0;
        double totalTax = 0.0;

        System.out.println("Items:");
        for (Product p : printReq.getProductList()) {
            double itemTotal = p.getPrice() * p.getQuantity();
            subtotal += itemTotal;

            double tax = calculateTax(p, printReq.getState());
            totalTax += tax;

            System.out.printf("- %s $%s x %d : $%.2f%n", p.getName(), p.getPrice(), p.getQuantity(), itemTotal);
        }

        double total = subtotal + totalTax;

        System.out.printf("Subtotal: $%.2f%n", subtotal);
        System.out.printf("Sales Tax: $%.2f%n", totalTax);
        System.out.printf("Total: $%.2f%n", total);
    }

    private static double roundTax(double tax) {
        return roundTo2Decimal(Math.ceil(tax * 20.0) / 20.0);
    }

    public static double roundTo2Decimal(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public double calculateTax(Product product, String state) {
        double itemTotal = product.getPrice() * product.getQuantity();
        double taxRate = TaxConfigRepository.getTaxRate(state);

        if (0.0 == taxRate) {
            return 0.0;  // 如果稅率是 0，則不需要計算稅金
        }

        boolean isExempt = TaxConfigRepository.getTaxExemptCategories(state)
                .contains(product.getCategory().toLowerCase());

        return isExempt ? 0.0 : roundTax(itemTotal * taxRate);
    }
}
