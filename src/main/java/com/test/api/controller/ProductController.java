package com.test.api.controller;

import com.test.api.exception.NotFoundException;
import com.test.api.model.Product;
import com.test.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/v1/products")
@Tag(name = "Product API", description = "This API allows to consult/modify product Catalogn")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getProducts() {
        return ResponseEntity.ok(service.getProductList());
    }

    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Gets the product information based on an id ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Product.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "product not found"),
                    @ApiResponse(responseCode = "500", description = "error pulling information")
            },
            parameters = {@Parameter(name = "productId", description = "identifier of product")})
    public ResponseEntity getProduct(@PathVariable String productId) {
        try {
            return ResponseEntity.ok(service.getProduct(productId));
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Updated or create data based on the identifier",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Product.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "invalid data")
            },
            parameters = {@Parameter(name = "id", description = "identifier of profile")})
    public ResponseEntity createOrUpdateProduct(@RequestBody @NotNull @NotEmpty Product product) {
        return ResponseEntity.ok(service.createOrUpdateProduct(product));
    }

    @DeleteMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Delete product",
            responses = {
                    @ApiResponse(
                            responseCode = "204"),
                    @ApiResponse(responseCode = "404", description = "product not found"),
                    @ApiResponse(responseCode = "400", description = "invalid data")
            },
            parameters = {@Parameter(name = "productId", description = "identifier of profile")})
    public ResponseEntity deleteProduct(@PathVariable String productId) {
        try {
            service.deleteProduct(productId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
