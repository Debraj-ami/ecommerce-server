package com.debraj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.debraj.exception.ProductException;
import com.debraj.model.Product;
import com.debraj.request.CreateProductRequest;
import com.debraj.service.ProductService;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    
    // CREATE PRODUCT
    @PostMapping("/")
    public ResponseEntity<Product> createProductHandler(
            @RequestBody CreateProductRequest req,
            @RequestHeader("Authorization") String jwt) {

        Product product = productService.createProduct(req);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    
    // CREATE MULTIPLE PRODUCTS
    @PostMapping("/creates")
    public ResponseEntity<List<Product>> createMultipleProductHandler(
            @RequestBody List<CreateProductRequest> reqs,
            @RequestHeader("Authorization") String jwt) {

        List<Product> products = productService.createMultipleProducts(reqs);

        return new ResponseEntity<>(products, HttpStatus.CREATED);
    }

    
    // DELETE PRODUCT
    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<String> deleteProductHandler(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt) throws ProductException {

        String message = productService.deleteProduct(productId);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    
    // UPDATE PRODUCT
    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProductHandler(
            @PathVariable Long productId,
            @RequestBody Product req,
            @RequestHeader("Authorization") String jwt) throws ProductException {

        Product updatedProduct = productService.updateProduct(productId, req);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    
    // FIND PRODUCT BY ID
    @GetMapping("/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(
            @PathVariable Long productId) throws ProductException {

        Product product = productService.findProductById(productId);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    
    // FIND PRODUCT BY CATEGORY
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> findProductByCategoryHandler(
            @PathVariable String category) {

        List<Product> products = productService.findProductByCategory(category);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    
    // GET ALL PRODUCTS WITH FILTERS
    @GetMapping("/")
    public ResponseEntity<Page<Product>> getAllProductsHandler(

            @RequestParam(required = false) String category,

            @RequestParam(required = false) List<String> colors,

            @RequestParam(required = false) List<String> sizes,

            @RequestParam(required = false) Integer minPrice,

            @RequestParam(required = false) Integer max,

            @RequestParam(required = false) Integer minDiscount,

            @RequestParam(required = false) String sort,

            @RequestParam(required = false) String stock,

            @RequestParam(defaultValue = "0") Integer pageNumber,

            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Product> products = productService.getAllProduct(
                category,
                colors,
                sizes,
                minPrice,
                max,
                minDiscount,
                sort,
                stock,
                pageNumber,
                pageSize);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}