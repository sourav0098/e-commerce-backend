package com.quickpik.services;

import com.quickpik.dtos.CategoryDto;
import com.quickpik.dtos.PageableResponse;

public interface CategoryService {
	CategoryDto createCategory(CategoryDto categoryDto);

	CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

	void deleteCategory(String categoryId);

	CategoryDto getCategoryById(String categoryId);

	PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);
}