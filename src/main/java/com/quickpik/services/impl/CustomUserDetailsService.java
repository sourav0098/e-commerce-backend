package com.quickpik.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.quickpik.entities.User;
import com.quickpik.repositories.UserRepository;

/**
 * Custom implementation of the UserDetailsService interface for Spring
 * Security.
 * @author Sourav Choudhary
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	/**
	 * UserRepository instance to be used for retrieving user details.
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * Loads a user by the given username (email) and returns a UserDetails object.
	 *
	 * @param username the email of the user to be loaded
	 * @return a UserDetails object representing the loaded user
	 * @throws UsernameNotFoundException if the user with the given email is not
	 *                                   found
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with given email not found"));
		return user;
	}
}