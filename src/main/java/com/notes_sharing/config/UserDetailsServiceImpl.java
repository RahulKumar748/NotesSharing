package com.notes_sharing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.notes_sharing.entity.UserDtls;
import com.notes_sharing.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDtls user = userRepo.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("User Not Exist");
		} else {
			CustomUserDtls customUserDtls = new CustomUserDtls(user);
			return customUserDtls;
		}

	}

}
