package com.example.demo.controller;

import com.example.demo.controller.req.PrintReq;
import com.example.demo.entity.Product;
import com.example.demo.repository.TaxConfigRepository;
import com.example.demo.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptServiceTest {

    private ReceiptService receiptService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        receiptService = new ReceiptService();
    }

    @Test
    public void testPrintReceipt() {
        // 設置測試用的資料
        Product product1 = new Product("book", 17.99, 1, "other");
        Product product2 = new Product("potato chips", 3.99, 1, "food");

        PrintReq printReq = new PrintReq("CA",Arrays.asList(product1, product2));

        // 設置輸出流來捕獲輸出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);  // 將標準輸出重定向到 outputStream

        // 調用 printReceipt 方法
        receiptService.printReceipt(printReq);

        // 捕獲的輸出
        String output = outputStream.toString();

        // 檢查每個金額的正確性：
        assertTrue(output.contains("Subtotal: $21.98"));
        assertTrue(output.contains("Sales Tax: $1.80"));
        assertTrue(output.contains("Total: $23.78"));
    }

    @Test
    public void testCalculateTax() {
        // 設置測試用的資料
        Product productNY = new Product("Laptop", 1000.0, 1, "other");
        Product productCA = new Product("Laptop", 1000.0, 1, "other");
        Product productFee = new Product("Food", 100.0, 1, "food");

        // 使用 `TaxConfigRepository` 直接在測試中調用
        double tax1 = receiptService.calculateTax(productNY, "NY");
        double tax2 = receiptService.calculateTax(productCA, "CA");
        double tax3 = receiptService.calculateTax(productFee, "CA");

        // 驗證稅金的正確性
        double CATax = 1000.0 * 0.0975;
        double NYTax = 1000.0 * 0.08875;
        double feeTax = 0.0;

        assertEquals(NYTax, tax1, 0.01);
        assertEquals(CATax, tax2, 0.01);
        assertEquals(feeTax, tax3, 0.01);
    }

    @Test
    public void testGetTaxRate() {
        assertEquals(0.0975, TaxConfigRepository.getTaxRate("CA"));
        assertEquals(0.08875, TaxConfigRepository.getTaxRate("NY"));
        assertEquals(0.0, TaxConfigRepository.getTaxRate("TX"));
    }

    @Test
    public void testGetTaxExemptCategories() {
        Set<String> caExemptCategories = TaxConfigRepository.getTaxExemptCategories("CA");
        Set<String> nyExemptCategories = TaxConfigRepository.getTaxExemptCategories("NY");
        Set<String> txExemptCategories = TaxConfigRepository.getTaxExemptCategories("TX");

        assertTrue(caExemptCategories.contains("food"));
        assertFalse(caExemptCategories.contains("clothing"));

        assertTrue(nyExemptCategories.contains("food"));
        assertTrue(nyExemptCategories.contains("clothing"));

        assertTrue(txExemptCategories.isEmpty());
    }
}
