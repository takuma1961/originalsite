package com.example.demo.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.User;

public class CustomUserDetails implements UserDetails {
	private final Long userId;
	private final String email;
	private final String password;
	private final boolean enabled;
	private final Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
		this.userId = user.getId(); // ここでIDを保持
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.enabled = user.isEnabled();
		this.authorities = authorities;
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
