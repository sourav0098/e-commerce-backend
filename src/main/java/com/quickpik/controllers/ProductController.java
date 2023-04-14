package com.quickpik.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
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

import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.ProductDto;
import com.quickpik.payload.ApiResponse;
import com.quickpik.services.FileService;
import com.quickpik.services.ProductService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Value("${product-image-path}")
	private String imageUploadPath;

	@Autowired
	private ProductService productService;

	@Autowired
	private FileService fileService;

	// get single
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
		ProductDto responseProductDto = this.productService.getProductById(productId);
		return new ResponseEntity<ProductDto>(responseProductDto, HttpStatus.OK);
	}

	// get all
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<ProductDto> response = this.productService.getAllProducts(pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(response, HttpStatus.OK);
	}

	// get all:live
	@GetMapping("/live")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProductsLive(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<ProductDto> response = this.productService.getAllProductsLive(pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(response, HttpStatus.OK);
	}

	// search title
	@GetMapping("/search/{title}")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProductsLive(@PathVariable String title,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<ProductDto> response = this.productService.searchProductByTitle(title, pageNumber, pageSize,
				sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(response, HttpStatus.OK);
	}

	// create
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
		ProductDto responseProductDto = this.productService.createProduct(productDto);
		return new ResponseEntity<ProductDto>(responseProductDto, HttpStatus.CREATED);
	}

	// update
	@PutMapping("/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto,
			@PathVariable String productId) {
		ProductDto updatedProductDto = this.productService.updateProduct(productDto, productId);
		return new ResponseEntity<ProductDto>(updatedProductDto, HttpStatus.OK);
	}

	// delete
	@DeleteMapping("/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId) {
		this.productService.deleteProduct(productId);
		ApiResponse response = ApiResponse.builder().message("Product deleted successfully")
				.status(HttpStatus.OK.value()).timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// upload image
	@PostMapping("/image/{productId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> uploadProductImage(@PathVariable String productId,
			@RequestParam("product-image") MultipartFile productImage) {

		try {
			String imageName = this.fileService.uploadImage(productImage, imageUploadPath);
			ProductDto productDto = this.productService.getProductById(productId);
			productDto.setProductImage(imageName);
			this.productService.updateProduct(productDto, productId);
			ApiResponse apiResponse = ApiResponse.builder().message(imageName).status(HttpStatus.CREATED.value())
					.timestamp(System.currentTimeMillis()).build();
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.CREATED);

		} catch (IOException e) {
			e.printStackTrace();
			ApiResponse apiResponse = ApiResponse.builder().message("Something went wrong! Please try again")
					.status(HttpStatus.BAD_REQUEST.value()).timestamp(System.currentTimeMillis()).build();
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		}
	}

	// serve image
	@GetMapping("/image/{productId}")
	public void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
		ProductDto productDto = this.productService.getProductById(productId);
		InputStream resource = fileService.getResource(imageUploadPath, productDto.getProductImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}