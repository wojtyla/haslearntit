/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.security;

import it.haslearnt.user.User;

import java.util.*;

import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;

@SuppressWarnings("serial")
public class AuthenticationUserDetails implements UserDetails {
	private final String login;
	private final String passwordHash;
	private final boolean enabled;
	private HashSet<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();

	public AuthenticationUserDetails(User user) {
		this.login = user.email();
		this.passwordHash = user.password();
		this.enabled = true;
		this.grantedAuthorities.addAll(new HashSet<GrantedAuthority>());
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return passwordHash;
	}

	@Override
	public String getUsername() {
		return login;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
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

	public String getLogin() {
		return login;
	}
}