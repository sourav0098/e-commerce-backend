package com.quickpik.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.quickpik.entities.Category;
import com.quickpik.entities.Product;


public interface ProductRepository extends JpaRepository<Product, String> {
	// search by title
	Page<Product> findByTitleContaining(String title, Pageable pageable);
	
	// Products which are live
	Page<Product> findByLiveTrue(Pageable pageable);
	
	// Find By Category
	Page<Product> findByCategory(Category category,Pageable pageable);
	
	// search by price range
}
