package com.quickpik.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	@Id
	private String orderId;

	private String orderNumber;
	
	// PENDING, DISPATCHED, DELIVERED
	@Column(nullable = false, columnDefinition = "varchar(20) default 'PENDING'")	
	private String orderStatus;

	// NOT-PAID, PAID
	@Column(nullable = false, columnDefinition = "varchar(10) default 'NOT-PAID'")	
	private String paymentStatus;

	@Column(nullable = false)
	private double orderAmount;

	@Column(nullable = false)
	private String shippingName;

	@Column(nullable = false)
	private String shippingAddress;

	@Column(length = 6, nullable = false)
	private String postalCode;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String province;

	@Column(length=10,nullable = false)
	private String shippingPhone;

	@Column(nullable = true)
	private Date deliveredDate;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Date createdAt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id",nullable = false)
	private User user;

	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();
}