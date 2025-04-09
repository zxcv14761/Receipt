package com.example.demo.repository;


import java.util.Set;

public class TaxConfigRepository {
    public static double getTaxRate(String state) {
        return switch (state.toUpperCase()) {
            case "CA" -> 0.0975;
            case "NY" -> 0.08875;
            default -> 0.0;
        };
    }

    public static Set<String> getTaxExemptCategories(String state) {
        return switch (state.toUpperCase()) {
            case "CA" -> Set.of("food");
            case "NY" -> Set.of("food", "clothing");
            default -> Set.of();
        };
    }
}
