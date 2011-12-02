/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.security;

import it.haslearnt.model.User;
import it.haslearnt.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationDetailsGetterUnitTest {

    @Mock
    UserRepository userRepository;

    String email = "email";
    String hashedPassword = "oidjgs";

    @Test
    public void shouldLoadUserByUsername() {
        //given
        AuthenticationDetailsGetter authenticationDetailsGetter = new AuthenticationDetailsGetter(userRepository);
        User user = new User(email, hashedPassword);
        given(userRepository.findByEmail(email)).willReturn(user);

        //when
        UserDetails userDetails = authenticationDetailsGetter.loadUserByUsername(email);

        //then
        assertEquals(email, userDetails.getUsername());
        assertEquals(hashedPassword, userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowExceptionIfUserNotFound() {
        //given
        AuthenticationDetailsGetter authenticationDetailsGetter = new AuthenticationDetailsGetter(userRepository);

        //when
        UserDetails userDetails = authenticationDetailsGetter.loadUserByUsername(email);

        //then exception is thrown
    }
}
