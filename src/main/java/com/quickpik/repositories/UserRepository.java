package com.quickpik.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.quickpik.entities.User;

public interface UserRepository extends JpaRepository<User, String>{
	// This method finds a user by email
	Optional<User> findByEmail(String email);
	
	// This method finds users whose first name contains the specified keyword
	List<User> findByFnameContaining(String keyword);
	
	User findUserByEmailIgnoreCase(String email);
    
}
