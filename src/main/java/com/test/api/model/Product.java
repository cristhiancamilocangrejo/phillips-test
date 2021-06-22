package com.test.api.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Product {
    private String id;
    private String name;
    private int quantity;
    private int price;
}
