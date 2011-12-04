/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.security;

import static org.springframework.util.Assert.notNull;
import it.haslearnt.user.*;
import it.haslearnt.user.User;

import org.springframework.dao.*;
import org.springframework.security.core.userdetails.*;

public class AuthenticationDetailsGetter implements UserDetailsService {
	private UserRepository userRepository;

	public AuthenticationDetailsGetter(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException {
		notNull(email);
		User user = userRepository.load(email);
		throwExceptionIfNotFound(user, email);
		return new AuthenticationUserDetails(user);
	}

	private void throwExceptionIfNotFound(User user, String email) {
		if (user == null) {
			throw new UsernameNotFoundException("User with email " + email + "  has not been found.");
		}
	}
}
