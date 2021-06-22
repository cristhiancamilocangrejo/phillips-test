package com.test.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.test.api.model.Product;

import java.util.List;

public interface ProductConsumer {

    List<Product> processProducts(JsonNode node);
}
