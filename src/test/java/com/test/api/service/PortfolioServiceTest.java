package com.test.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.test.api.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class PortfolioServiceTest {

    private final String URL_API = "http://localhost:8080";

    private ProductService service;

    private RestTemplate restTemplate = mock(RestTemplate.class);

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        service = new ProductService(restTemplate, mapper, URL_API);
    }

    @Test
    public void testGetAllProducts() {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.putArray("bundle");

        when(restTemplate.exchange(URL_API, HttpMethod.GET, HttpEntity.EMPTY, ObjectNode.class))
                .thenReturn(ResponseEntity.ok(objectNode));

        List<Product> products = service.getProductList();
        assertNotNull(products);
        verify(restTemplate).exchange(URL_API, HttpMethod.GET, HttpEntity.EMPTY, ObjectNode.class);
    }

    @Test
    public void createProduct() {

        Product product = Product.builder().id("abc-123").name("test").price(100).quantity(200).build();
        when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok(product));

        product = service.createOrUpdateProduct(product);

        assertNotNull(product);
        verify(restTemplate).exchange(URL_API, HttpMethod.POST, new HttpEntity<>(product), Product.class);
    }

    @Test
    public void deleteProduct() {

        String productId = "abc-123";
        when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(ResponseEntity.noContent().build());

        service.deleteProduct(productId);

        verify(restTemplate).exchange(URL_API +"/"+productId, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
    }
}
