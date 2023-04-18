package com.quickpik.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.quickpik.security.JwtAuthenticationEntryPoint;
import com.quickpik.security.JwtAuthenticationFilter;

/**
 * Configuration class for Spring Security.
 * 
 * @author Sourav Choudhary
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	/**
	 * UserDetailsService instance to be used by the authentication provider.
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtAuthenticationEntryPoint autenticationEntryPoint;

	@Autowired
	private JwtAuthenticationFilter authenticationFilter;

	/**
	 * A method that configures the Spring Security filter chain to enable
	 * authentication and authorization of incoming HTTP requests. This method
	 * disables CSRF protection and CORS support, requires all requests to be
	 * authenticated, and sets the authentication entry point and session creation
	 * policy. Additionally, it adds a custom authentication filter to the filter
	 * chain to handle user authentication.
	 **/
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().disable().authorizeHttpRequests()
				.requestMatchers("/auth/login").permitAll()
				.requestMatchers("/auth/google").permitAll()
				.requestMatchers(HttpMethod.POST, "/users").permitAll()
				.requestMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")
				.anyRequest().authenticated().and()
				.exceptionHandling().authenticationEntryPoint(autenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	/**
	 * Creates and returns an instance of DaoAuthenticationProvider to be used by
	 * Spring Security.
	 * 
	 * @return DaoAuthenticationProvider instance with UserDetailsService and
	 *         PasswordEncoder set
	 */
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	/**
	 * Creates and returns an instance of BCryptPasswordEncoder as the
	 * PasswordEncoder implementation.
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
