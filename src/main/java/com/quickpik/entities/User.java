package com.quickpik.entities;

import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
	@Id
	@Column(name = "user_id")
	private String userId;
	
	@Column(nullable=false)
	private String fname;
	
	@Column(nullable=false)
	private String lname;

	@Column(unique = true, nullable = false)
	private String email;
	
	private String password;
	
	@Column(nullable = true)
	private String phone;

	@Column(nullable = true)
	private String address;

	@Column(name = "postal_code", length = 6, nullable = true)
	private String postalCode;
	
	@Column(nullable = true)
	private String city;
	
	@Column(nullable = true)
	private String province;
	
	@Column(nullable = true)
	private String image;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Date updatedAt;
}