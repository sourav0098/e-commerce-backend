package com.quickpik.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

	@Column(length = 150, nullable = false)
	private String shortDescription;

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
	private boolean live;

	@Column(nullable = false)
	private boolean stock;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private Date updatedAt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "categoryId")
	private Category category;
}