package com.debraj.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debraj.exception.ProductException;
import com.debraj.model.Product;
import com.debraj.request.CreateProductRequest;
import com.debraj.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    // Constructor Injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // CREATE PRODUCT
    @PostMapping("/admin/products")
    public ResponseEntity<Product> createProductHandler(@RequestBody CreateProductRequest req) {

        Product createdProduct = productService.createProduct(req);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // GET PRODUCTS BY CATEGORY
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> color,
            @RequestParam(required = false) List<String> size,
            @RequestParam(defaultValue = "0") Integer minPrice,
            @RequestParam(defaultValue = "100000") Integer maxPrice,
            @RequestParam(defaultValue = "0") Integer minDiscount,
            @RequestParam(defaultValue = "price_low") String sort,
            @RequestParam(required = false) String stock,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Product> res = productService.getAllProduct(
                category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize
        );

        System.out.println("complete products");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    // GET PRODUCT BY ID
    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {

        Product product = productService.findProductById(productId);

        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }
    
    // CREATE MULTIPLE PRODUCTS
    @PostMapping("/admin/products/bulk")
    public ResponseEntity<List<Product>> createMultipleProducts(
            @RequestBody List<CreateProductRequest> reqs) {

        List<Product> products = productService.createMultipleProducts(reqs);

        return new ResponseEntity<>(products, HttpStatus.CREATED);
    }
}