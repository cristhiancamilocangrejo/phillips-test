package com.test.api.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.test.api.SpringApplication;
import com.test.api.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = SpringApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockInitializer.class})
public class PortfolioControllerIntegrationTest {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    @AfterEach
    public void afterEach() {
        this.wireMockServer.resetAll();
    }

    @Test
    public void testGetProducts() {
        this.wireMockServer.stubFor(
                WireMock.get("/products")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\"bundle\":[{\"quantity\":3,\"id\":\"fd005736-c840-47ea-a471-f98d228c1c5e\",\"price\":100,\"name\":\"testxxxx444xx\"},{\"quantity\":1,\"id\":\"4dbbe4e7-9716-4843-8e4c-d93f1b92cea5\",\"price\":100,\"name\":\"mqmSrgBKbv00002\"},{\"id\":\"c29b2c19-4e13-4d3c-9735-3db54b96c274\"},{\"quantity\":3,\"id\":\"311\",\"price\":451854,\"name\":\"Fridge\"},{\"quantity\":0,\"id\":\"815a9901-2f68-465b-9fa0-daa40fb69199\",\"price\":0,\"name\":\"\"},{\"quantity\":11,\"id\":\"12122\",\"price\":10.9,\"name\":\"name27name\"},{\"quantity\":23,\"id\":\"product23\",\"price\":10.4,\"name\":\"name23\"},{\"id\":\"9667dda4-9f7b-48ec-bbfe-df15850bc403\"},{\"quantity\":1,\"id\":\"6fa7ba32-831a-49e0-83ff-07f3f7a19669\",\"price\":100,\"name\":\"mqmSrgBKbv00002\"},{\"quantity\":4,\"id\":\"1c857f47-300f-46f8-b496-df9e657c80ed\",\"price\":1,\"name\":\"test\"},{\"id\":\"e3756948-4abe-4a9c-af4d-08e43e73156a\",\"tst\":3},{\"id\":\"8805f723-24ed-4b88-b303-bc81b30d6565\"},{\"quantity\":3,\"id\":\"e38cc5cf-4263-4298-9beb-383e55ee7133\",\"price\":5,\"name\":\"testxxxx444xx\"},{\"quantity\":3,\"id\":\"6367f719-33aa-423f-938e-2752d6959cfa\",\"price\":5,\"name\":\"testxxxx444xx\"},{\"quantity\":5,\"id\":\"ramesh\",\"price\":890,\"name\":\"jai\"},{\"quantity\":3,\"id\":\"8f0f3de9-20b2-4abe-8607-d704724d74ea\",\"price\":100,\"name\":\"testxxxx444xx\"},{\"quantity\":3,\"id\":\"775f181b-d8b4-47dc-9337-48cf369180dc\",\"price\":5,\"name\":\"testxxxx444xx\"},{\"id\":\"edb1562d-80c0-46d8-8bd3-f98428e31f36\"},{\"id\":\"e7376596-a860-4dc5-b2bc-a18b41a45813\",\"tst\":3},{\"quantity\":3,\"id\":\"TYESTUD\",\"price\":5,\"name\":\"testxxxx444xx\"},{\"id\":\"05e10c49-5b57-4006-9402-4438c0249fbf\"},{\"quantity\":5,\"id\":\"RannVijay\",\"price\":890,\"name\":\"Flowers\"},{\"quantity\":5,\"id\":\"Andy2\",\"price\":890,\"name\":\"Flowers\"},{\"quantity\":1,\"id\":\"422182cb-3db5-444b-b944-8356f0eba8bc\",\"price\":100,\"name\":\"mqmSrgBKbv00002\"},{\"quantity\":3,\"id\":\"608d8d58-0081-48b0-b8a4-3e889c1b903d\",\"price\":5,\"name\":\"testxxxx444xx\"},{\"id\":\"50cb855f-3868-4d07-a524-6b40a39c4740\"},{\"id\":\"2325f5f0-c043-4b97-a8bf-78bb39820dbd\"},{\"quantity\":5,\"id\":\"shobith\",\"price\":100,\"name\":\"prdfgtd2\"},{\"quantity\":2,\"id\":\"Garry\",\"price\":345325,\"name\":\"Iphone\"},{\"quantity\":5,\"id\":\"Andy\",\"price\":890,\"name\":\"Flowers\"},{\"quantity\":1,\"id\":\"3900da61-8dcb-4222-b4cf-0a1f49765f05\",\"price\":100,\"name\":\"mqmSrgBKbv00002\"},{\"quantity\":5,\"id\":\"balu\",\"price\":100,\"name\":\"prdfgtd2\"},{\"id\":\"7095391f-4221-47ed-8adc-ec61027697e0\"},{\"id\":\"c2713b45-750c-4bd7-8a2b-5036a987c2fd\"},{\"id\":\"f9c95292-5310-45e9-ba07-e54f91eaf391\"},{\"quantity\":100,\"id\":\"supertest1\",\"price\":100,\"name\":\"supertest1\"}]}"))
        );


        ResponseEntity products = restTemplate.exchange("http://localhost:" + port + "/v1/products", HttpMethod.GET, HttpEntity.EMPTY, JsonNode.class);
        assertNotNull(products);
        assertNotNull(products.getBody());
    }

    @Test
    public void testCreateProduct() {
        this.wireMockServer.stubFor(
                WireMock.post("/products")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\"quantity\":5,\"id\":\"RannVijay\",\"price\":890,\"name\":\"Flowers\"}")));
        Product product = Product.builder().id("abc-123").name("test").price(100).quantity(200).build();
        ResponseEntity response = restTemplate.postForEntity("http://localhost:" + port + "/v1/products", product, Product.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    public void testDeleteProduct() {
        this.wireMockServer.stubFor(
                WireMock.delete("/products")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
        restTemplate.delete("http://localhost:" + port + "/v1/products");
    }


    @Test
    public void testGetProduct() {
        String productId = "RannVijay";
        this.wireMockServer.stubFor(
                WireMock.get("/products/" + productId)
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\"quantity\":5,\"id\":\"RannVijay\",\"price\":890,\"name\":\"Flowers\"}")));
        ResponseEntity response = restTemplate.getForEntity("http://localhost:" + port + "/v1/products"+productId, Product.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
    }
}