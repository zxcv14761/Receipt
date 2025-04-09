package com.example.demo.controller.req;

import com.example.demo.entity.Product;
import lombok.Value;

import java.util.List;

@Value
public class PrintReq {

    String state;

    List<Product> productList;

}
