package com.debraj.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.debraj.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ✅ Check duplicate product
    List<Product> findByTitleAndBrandAndColor(String title, String brand, String color);

    boolean existsByTitleAndBrandAndColor(String title, String brand, String color);

    // ✅ PAGINATION + FILTER QUERY (FINAL)
    @Query("""
    	    SELECT p FROM Product p
    	    WHERE (
    	        :category IS NULL 
    	        OR :category = '' 
    	        OR LOWER(p.category.name) = LOWER(:category)
    	    )
    	    AND (
    	        (:minPrice IS NULL AND :maxPrice IS NULL)
    	        OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)
    	    )
    	    AND (
    	        :minDiscount IS NULL 
    	        OR p.discountPercent >= :minDiscount
    	    )
    	""")
    	Page<Product> filterProducts(
    	        @Param("category") String category,
    	        @Param("minPrice") Integer minPrice,
    	        @Param("maxPrice") Integer maxPrice,
    	        @Param("minDiscount") Integer minDiscount,
    	        Pageable pageable
    	);
}