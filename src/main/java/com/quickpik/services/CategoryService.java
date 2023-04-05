package com.quickpik.services;

import com.quickpik.dtos.CategoryDto;
import com.quickpik.dtos.PageableResponse;

public interface CategoryService {

	// Creates a new category and returns the created category DTO
	CategoryDto createCategory(CategoryDto categoryDto);

	// Updates an existing category with the provided category DTO and returns the updated category DTO
	CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

	// Deletes a category with the specified category ID
	void deleteCategory(String categoryId);

	// Returns the category DTO for the specified category ID
	CategoryDto getCategoryById(String categoryId);

	// Returns a pageable response containing all categories, sorted and filtered according to the specified parameters
	PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);
}
