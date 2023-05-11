package com.quickpik.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.quickpik.entities.User;

public interface UserRepository extends JpaRepository<User, String>{
	// This method finds a user by email
	Optional<User> findByEmail(String email);
	
	// This method finds users whose first name contains the specified keyword
	Page<User> findByFnameContaining(String keyword, Pageable pageable);
	
	User findUserByEmailIgnoreCase(String email);
    
}
