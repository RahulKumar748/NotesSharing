package com.notes_sharing.config;


import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.notes_sharing.entity.UserDtls;

public class CustomUserDtls implements UserDetails {

	
	private UserDtls userdtls;
	public CustomUserDtls(UserDtls userdtls) {
		super();
		this.userdtls = userdtls;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userdtls.getRole());

		return List.of(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return userdtls.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userdtls.getEmail();
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
