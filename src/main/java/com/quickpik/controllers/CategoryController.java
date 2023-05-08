package com.quickpik.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.quickpik.dtos.CategoryDto;
import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.ProductDto;
import com.quickpik.payload.ApiResponse;
import com.quickpik.services.CategoryService;
import com.quickpik.services.ImageService;
import com.quickpik.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Value("${aws.s3.categories-image-path}")
	private String categoryImageUploadPath;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ProductService productService;

	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
		CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "categoryTitle", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<CategoryDto> response = this.categoryService.getAllCategories(pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<PageableResponse<CategoryDto>>(response, HttpStatus.OK);
	}

	@GetMapping("/{categoryId}/products")
	public ResponseEntity<PageableResponse<ProductDto>> getProductsByCategoryId(@PathVariable String categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "brand", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<ProductDto> response = this.productService.getAllProductsByCategory(categoryId, pageNumber,
				pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(response, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto categoryResposne = this.categoryService.createCategory(categoryDto);
		return new ResponseEntity<>(categoryResposne, HttpStatus.CREATED);
	}

	// Create product with category
	@PostMapping("/{categoryId}/products")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductDto> createProductWithCategory(@Valid @RequestBody ProductDto productDto,
			@PathVariable String categoryId) {
		ProductDto productResponse = this.productService.createProductWithCategory(productDto, categoryId);
		return new ResponseEntity<ProductDto>(productResponse, HttpStatus.CREATED);
	}

	@PutMapping("/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable("categoryId") String categoryId) {
		CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, categoryId);
		return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
	}

	@PutMapping("/{categoryId}/products/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable String productId,
			@PathVariable("categoryId") String categoryId) {
		ProductDto updatedProduct = this.productService.updateProductCategory(categoryId, productId);
		return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
	}

	@DeleteMapping("/{categoryId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") String categoryId) {
		this.categoryService.deleteCategory(categoryId);
		ApiResponse response = ApiResponse.builder().message("Category deleted successfully")
				.status(HttpStatus.OK.value()).timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Upload category image
	@PostMapping("/image/{categoryId}")
	public ResponseEntity<ApiResponse> uploadCategoryImage(@PathVariable("categoryId") String categoryId,
			@RequestParam("image") MultipartFile image) {
		String imageName;
		try {

			// upload image and returns image name
			imageName = this.imageService.uploadImage(image, categoryImageUploadPath);

			// get category by id and set the image name for the category
			CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);
			categoryDto.setCategoryImage(imageName);
			categoryService.updateCategory(categoryDto, categoryId);

			// Build the API response with success message and status code
			ApiResponse apiResponse = ApiResponse.builder().message(imageName).status(HttpStatus.CREATED.value())
					.timestamp(System.currentTimeMillis()).build();
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.CREATED);
		} catch (IOException e) {
			e.printStackTrace();
			// Build the API response with error message and status code
			ApiResponse apiResponse = ApiResponse.builder().message("Something went wrong! Please try again")
					.status(HttpStatus.BAD_REQUEST.value()).timestamp(System.currentTimeMillis()).build();
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		}
	}

	// Serve images
	@GetMapping("/image/{categoryId}")
	public ResponseEntity<ApiResponse> serveCategoryImage(@PathVariable String categoryId) throws IOException {
		// get the category by category id
		CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);
		String imageName = this.imageService.getImageUrl(categoryDto.getCategoryImage(), categoryImageUploadPath);
		ApiResponse apiResponse = ApiResponse.builder().message(imageName).status(HttpStatus.OK.value())
				.timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}
}