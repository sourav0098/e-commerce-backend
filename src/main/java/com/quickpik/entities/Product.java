package com.quickpik.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
	@Id
	private String productId;

	@Column(nullable = false)
	private String brand;

	@Column(nullable = false)
	private String title;

	@Column(length = 10000, nullable = false)
	private String description;

	@Column(nullable = false)
	private double unitPrice;

	@Column(columnDefinition = "double default 0")
	private double discountedPrice;

	@Column(nullable = false)
	private int quantity;
	
	private String productImage;

	@Column(nullable = false)
	private boolean isLive;

	@Column( nullable = false)
	private boolean isStock;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private Date updatedAt;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="categoryId")
	private Category category;
}