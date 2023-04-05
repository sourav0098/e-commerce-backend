package com.quickpik.services;

import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.ProductDto;

public interface ProductService {

	// Searches for products with the specified title, returning a pageable response
	// of product DTOs
	PageableResponse<ProductDto> searchProductByTitle(String title, int pageNumber, int pageSize, String sortBy,
			String sortDir);

	// Get a Product for the specified product ID
	ProductDto getProductById(String productId);

	// Returns a pageable response containing all products, sorted and filtered
	// according to the specified parameters
	PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);

	// Returns a pageable response containing all live products (i.e., products that
	// are currently available for purchase), sorted and filtered according to the
	// specified parameters
	PageableResponse<ProductDto> getAllProductsLive(int pageNumber, int pageSize, String sortBy, String sortDir);

	PageableResponse<ProductDto> getAllProductsByCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir);
	
	// Creates a new product
	ProductDto createProduct(ProductDto productDto);
	
	// Create product with category
	ProductDto createProductWithCategory(ProductDto productDto, String categoryId);

	// Updates an existing product
	ProductDto updateProduct(ProductDto productDto, String productId);
	
	// Update category of product
	ProductDto updateProductCategory(String categoryId,String productId);

	// Deletes a product with the specified product Id
	void deleteProduct(String productId);
}
