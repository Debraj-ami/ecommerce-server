package com.debraj.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.debraj.exception.ProductException;
import com.debraj.model.Category;
import com.debraj.model.Product;
import com.debraj.repository.CategoryRepository;
import com.debraj.repository.ProductRepository;
import com.debraj.request.CreateProductRequest;

@Service
public class ProductServiceImplementation implements ProductService {

    private ProductRepository productRepository;
    private UserService userService;
    private CategoryRepository categoryRepository;

    public ProductServiceImplementation(ProductRepository productRepository,
                                        UserService userService,
                                        CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    // ================= CREATE PRODUCT =================
    @Override
    public Product createProduct(CreateProductRequest req) {

        boolean exists = productRepository.existsByTitleAndBrandAndColor(
                req.getTitle(),
                req.getBrand(),
                req.getColor()
        );

        if (exists) {
            return productRepository
                    .findByTitleAndBrandAndColor(
                            req.getTitle(),
                            req.getBrand(),
                            req.getColor()
                    )
                    .get(0);
        }

        // CATEGORY LEVEL 1
        Category topLevel = categoryRepository.findByName(req.getTopLavelCategory());
        if (topLevel == null) {
            topLevel = new Category();
            topLevel.setName(req.getTopLavelCategory());
            topLevel.setLevel(1);
            topLevel = categoryRepository.save(topLevel);
        }

        // LEVEL 2
        Category secondLevel = categoryRepository
                .findByNameAndParant(req.getSecondLavelCategory(), topLevel.getName());

        if (secondLevel == null) {
            secondLevel = new Category();
            secondLevel.setName(req.getSecondLavelCategory());
            secondLevel.setParentCategory(topLevel);
            secondLevel.setLevel(2);
            secondLevel = categoryRepository.save(secondLevel);
        }

        // LEVEL 3
        Category thirdLevel = categoryRepository
                .findByNameAndParant(req.getThirdLavelCategory(), secondLevel.getName());

        if (thirdLevel == null) {
            thirdLevel = new Category();
            thirdLevel.setName(req.getThirdLavelCategory());
            thirdLevel.setParentCategory(secondLevel);
            thirdLevel.setLevel(3);
            thirdLevel = categoryRepository.save(thirdLevel);
        }

        Product product = new Product();

        product.setTitle(req.getTitle());
        product.setBrand(req.getBrand());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountPercent(req.getDiscountPersent());
        product.setImageUrl(req.getImageUrl());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        if (req.getSize() != null) {
            product.setSizes(new java.util.HashSet<>(req.getSize()));
        }

        return productRepository.save(product);
    }

    // ================= DELETE =================
    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Deleted";
    }

    // ================= UPDATE =================
    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {
        Product product = findProductById(productId);

        if (req.getQuantity() != 0) {
            product.setQuantity(req.getQuantity());
        }

        return productRepository.save(product);
    }

    // ================= FIND BY ID =================
    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> opt = productRepository.findById(id);

        if (opt.isPresent()) {
            return opt.get();
        }
        throw new ProductException("Product not found with id - " + id);
    }

    // ================= CATEGORY =================
    @Override
    public List<Product> findProductByCategory(String category) {
        return null;
    }

    // ================= MAIN FIXED METHOD =================
    @Override
    public Page<Product> getAllProduct(
            String category,
            List<String> colors,
            List<String> sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber,
            Integer pageSize
    ) {

        // ✅ STEP 1: Fetch all products (no pagination)
    	List<Product> products = productRepository.filterProducts(
    			category,
    	        minPrice,
    	        maxPrice,
    	        minDiscount,
    	        Pageable.unpaged()
    	).getContent();
    	// ✅ COLOR FILTER
    	if (colors != null && !colors.isEmpty()) {

    	    products = products.stream()
    	        .filter(p -> {

    	            if (p.getColor() == null) {
    	                return false;
    	            }

    	            String productColor = p.getColor().trim().toLowerCase();

    	            return colors.stream()
    	                .anyMatch(c ->
    	                    productColor.contains(c.trim().toLowerCase())
    	                );
    	        })
    	        .collect(Collectors.toList());
    	}

        // ✅ SIZE FILTER
        if (sizes != null && !sizes.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getSizes().stream()
                            .anyMatch(s -> sizes.stream()
                                    .anyMatch(req -> req.equalsIgnoreCase(s.getName()))))
                    .collect(Collectors.toList());
        }

        // ✅ STOCK FILTER
        if ("in_stock".equalsIgnoreCase(stock)) {
            products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
        } else if ("out_of_stock".equalsIgnoreCase(stock)) {
            products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
        }

        // ✅ SORTING
        if ("price_high".equals(sort)) {
            products = products.stream()
                    .sorted((a, b) -> b.getDiscountedPrice() - a.getDiscountedPrice())
                    .collect(Collectors.toList());
        } else {
            products = products.stream()
                    .sorted((a, b) -> a.getDiscountedPrice() - b.getDiscountedPrice())
                    .collect(Collectors.toList());
        }

        // ✅ SAFE PAGE NUMBER (IMPORTANT)
        int pageIndex = (pageNumber == null || pageNumber < 0) ? 0 : pageNumber;

        int start = pageIndex * pageSize;
        int end = Math.min(start + pageSize, products.size());

        List<Product> pageContent =
                (start >= products.size()) ? List.of() : products.subList(start, end);

        return new PageImpl<>(
                pageContent,
                PageRequest.of(pageIndex, pageSize),
                products.size()
        );
    }

    // ================= MULTIPLE CREATE =================
    @Override
    public List<Product> createMultipleProducts(List<CreateProductRequest> reqs) {

        List<Product> products = new java.util.ArrayList<>();

        for (CreateProductRequest req : reqs) {
            Product product = createProduct(req);
            if (product != null) {
                products.add(product);
            }
        }

        return products;
    }
}