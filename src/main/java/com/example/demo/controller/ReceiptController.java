package com.example.demo.controller;

import com.example.demo.controller.req.PrintReq;
import com.example.demo.service.ReceiptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("print")
    public void print(@RequestBody PrintReq printReq){
        receiptService.printReceipt(printReq);
    }
}
