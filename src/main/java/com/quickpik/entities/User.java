package com.quickpik.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class User implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String userId;

	@Column(nullable = false)
	private String fname;

	@Column(nullable = false)
	private String lname;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = true)
	private String phone;

	@Column(nullable = true)
	private String address;

	@Column(length = 6, nullable = true)
	private String postalCode;

	@Column(nullable = true)
	private String city;

	@Column(nullable = true)
	private String province;

	@Column(nullable = true)
	private String image;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private Date updatedAt;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "user")
	private List<Order> orders = new ArrayList<>();

	@OneToOne(mappedBy="user",cascade = CascadeType.REMOVE)
	private Cart cart;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Role> roles = new HashSet<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Set<GrantedAuthority> authorities = this.roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());
		return authorities;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}