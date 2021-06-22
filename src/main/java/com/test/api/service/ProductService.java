package com.test.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.test.api.model.Product;
import com.test.api.util.ProductConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProductService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String apiUrl;

    public ProductService(final RestTemplate restTemplate, final ObjectMapper mapper, @Value("${api.url}") final String apiUrl) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.apiUrl = apiUrl;
    }

    public List<Product> getProductList() {
        return getProductsFromResult(makeRequest(apiUrl, HttpMethod.GET, getEntity(Optional.empty()), ObjectNode.class).getBody(),
                json -> mapper.convertValue(json.get("bundle"), ArrayList.class));
    }

    private List<Product> getProductsFromResult(JsonNode result, ProductConsumer productConsumer) {
        return productConsumer.processProducts(result);
    }

    public Product createOrUpdateProduct(Product product) {
        return makeRequest(apiUrl, HttpMethod.POST, getEntity(Optional.of(product)), Product.class).getBody();
    }

    public Product getProduct(String productId) {
        return makeRequest(getUrl(productId), HttpMethod.GET, getEntity(Optional.empty()), Product.class).getBody();
    }

    public void deleteProduct(String productId) {
        makeRequest(getUrl(productId), HttpMethod.DELETE, getEntity(Optional.empty()), String.class);
    }

    @Retryable(value = HttpClientErrorException.class, backoff = @Backoff(delay = 2000))
    private <T> ResponseEntity<T> makeRequest(String url, HttpMethod method, HttpEntity body, Class<T> returnClass) {
        return restTemplate.exchange(url, method, body, returnClass);
    }

    private <T> HttpEntity<T> getEntity(Optional<T> body) {
        if (body.isPresent()) {
            return new HttpEntity(body.get());
        } else {
            return (HttpEntity<T>) HttpEntity.EMPTY;
        }
    }

    private String getUrl(String productId) {
        return apiUrl + "/" + productId;
    }
}
