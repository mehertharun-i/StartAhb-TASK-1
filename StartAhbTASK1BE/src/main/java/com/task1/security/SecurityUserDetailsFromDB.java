package com.task1.security;

import java.util.ArrayList;
import java.util.List;

import com.task1.dao.UserRepository;
import com.task1.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class SecurityUserDetailsFromDB implements UserDetailsService{

	private final UserRepository userRepository;
	
	public SecurityUserDetailsFromDB(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users user = userRepository.findByUserLoginId(username);
		
		return User.builder()
					.username(user.getUserLoginId())
					.password(user.getUserPassword())
					.build();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	//Database login Process using DAO Authentication Provider
	

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//		UserClass userLoginId = userClassRepository.findByUserLoginId(username);
//		
//		return User.builder()
//					.username(userLoginId.getUserLoginId())
//					.password(userLoginId.getUserPassword())
//					.build();
//	}

	
	
}
