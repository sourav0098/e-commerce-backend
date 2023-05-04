package com.quickpik.services.impl;

import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.ProductDto;
import com.quickpik.entities.Category;
import com.quickpik.entities.Product;
import com.quickpik.exception.ResourceNotFoundException;
import com.quickpik.helper.Helper;
import com.quickpik.repositories.CategoryRepository;
import com.quickpik.repositories.ProductRepository;
import com.quickpik.services.ImageService;
import com.quickpik.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Value("${aws.s3.products-image-path}")
	private String productImageUploadPath;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ModelMapper modelMappper;

	@Override
	public PageableResponse<ProductDto> searchProductByTitle(String title, int pageNumber, int pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).descending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = this.productRepository.findByTitleContaining(title, pageable);
		return Helper.getPageableResponse(page, ProductDto.class);
	}

	@Override
	public ProductDto getProductById(String productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("No product found!"));
		return modelMappper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).descending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = this.productRepository.findAll(pageable);
		return Helper.getPageableResponse(page, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProductsLive(int pageNumber, int pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).descending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = this.productRepository.findByLiveTrue(pageable);
		return Helper.getPageableResponse(page, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProductsByCategory(String categoryId, int pageNumber, int pageSize,
			String sortBy, String sortDir) {
		// fetch category
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("No category found"));
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
				: (Sort.by(sortBy).descending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = this.productRepository.findByCategory(category, pageable);
		return Helper.getPageableResponse(page, ProductDto.class);
	}

	@Override
	public ProductDto createProduct(ProductDto productDto) {
		// Generate UUID
		String productId = UUID.randomUUID().toString();
		productDto.setProductId(productId);

		Product product = modelMappper.map(productDto, Product.class);
		Product savedProduct = this.productRepository.save(product);
		return modelMappper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, String productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("No product found!"));
		product.setProductId(productId);
		product.setBrand(productDto.getBrand());
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setUnitPrice(productDto.getUnitPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setQuantity(productDto.getQuantity());
		product.setProductImage(productDto.getProductImage());
		product.setLive(productDto.isLive());
		product.setStock(productDto.isStock());

		Product savedProduct = this.productRepository.save(product);
		return modelMappper.map(savedProduct, ProductDto.class);
	}

	@Override
	public void deleteProduct(String productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("No product found!"));

		// delete product image
		this.imageService.deleteImage(product.getProductImage());
		this.productRepository.deleteById(productId);
	}

	@Override
	public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
		// fetch category
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("No category found"));

		// Generate UUID
		String productId = UUID.randomUUID().toString();
		productDto.setProductId(productId);

		Product product = modelMappper.map(productDto, Product.class);
		product.setCategory(category);
		Product savedProduct = this.productRepository.save(product);
		return modelMappper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProductCategory(String categoryId, String productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("No product found!"));
		// fetch category
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Product of given category found"));
		product.setCategory(category);

		Product savedProduct = this.productRepository.save(product);
		return modelMappper.map(savedProduct, ProductDto.class);
	}
}
