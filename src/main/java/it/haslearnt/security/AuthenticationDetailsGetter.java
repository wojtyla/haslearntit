/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.security;

import it.haslearnt.model.User;
import it.haslearnt.repositories.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.springframework.util.Assert.notNull;

public class AuthenticationDetailsGetter implements UserDetailsService {
    private UserRepository userRepository;

    public AuthenticationDetailsGetter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException {
        notNull(email);
        User user = userRepository.findByEmail(email);
        throwExceptionIfNotFound(user, email);
        return new AuthenticationUserDetails(user);
    }

    private void throwExceptionIfNotFound(User user, String email) {
        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + "  has not been found.");
        }
    }
}
