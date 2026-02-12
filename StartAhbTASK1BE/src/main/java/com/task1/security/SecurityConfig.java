package com.task1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	
	// JWT Login Process using JWT Authentication Provider
	
	@Autowired
	JWTAuthFilter jwtAuthFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public SecurityFilterChain secuirtyFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
						.csrf(csrf -> csrf.disable())
						.cors(cors -> cors.disable())
						.authorizeHttpRequests(auth -> auth.requestMatchers("/user/create", "/user/login", "/api/auth/**")
															.permitAll()
															.anyRequest()
															.authenticated())
						.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
						.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
						.build();													
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager(); 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// DataBases Login Process using DAO Authentication Provider 
	
	/*
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		return httpSecurity
						.authorizeHttpRequests(auth -> auth.requestMatchers("/user/insertuserdetails")
															.permitAll()
															.requestMatchers("/user/**", "/order/**")
															.authenticated()
															.anyRequest()
															.permitAll())
						.csrf(csrf -> csrf.disable())
						.httpBasic(Customizer.withDefaults())
						.formLogin(Customizer.withDefaults())
						.build();
	}
	*/
	
		
		
	
	//IN Memory User Details Store Process 
	
/*	@Bean
	public UserDetailsService userDetailsService() {
		
		String pass1 = passwordEncoder().encode("enduku");
		String pass2 = passwordEncoder().encode("cheppa");
		System.out.println("password 1 -> "+pass1);
		System.out.println("password 2 -> "+pass2);
		
		UserDetails user1 = User.builder()
								.username("meher")
								.password(pass1)
								.build();
		
		UserDetails user2 = User.builder()
								.username("tharun")
								.password(pass2)
								.build();
		
		return new InMemoryUserDetailsManager(user1, user2);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.authorizeHttpRequests(auth -> auth
													.requestMatchers("/order/**","/user/getuserorderdetails/{id}")
													.authenticated()
													.requestMatchers("/product/**")
													.permitAll()
													.requestMatchers("/user/**")
													.denyAll())
					.formLogin(Customizer.withDefaults())
					.csrf(cs -> cs.disable())
					.httpBasic(Customizer.withDefaults())
					.build();
					
	}
	*/
}
