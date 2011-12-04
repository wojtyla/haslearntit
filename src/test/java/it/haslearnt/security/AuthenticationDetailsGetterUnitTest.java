/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import it.haslearnt.user.*;
import it.haslearnt.user.User;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;
import org.springframework.security.core.userdetails.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationDetailsGetterUnitTest {

	@Mock
	UserRepository userRepository;

	String email = "email";
	String hashedPassword = "oidjgs";

	@Test
	public void shouldLoadUserByUsername() {
		// given
		AuthenticationDetailsGetter authenticationDetailsGetter = new AuthenticationDetailsGetter(userRepository);
		User user = new User().withEmail(email).withPassword(hashedPassword);
		given(userRepository.load(email)).willReturn(user);

		// when
		UserDetails userDetails = authenticationDetailsGetter.loadUserByUsername(email);

		// then
		assertEquals(email, userDetails.getUsername());
		assertEquals(hashedPassword, userDetails.getPassword());
		assertTrue(userDetails.isEnabled());
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldThrowExceptionIfUserNotFound() {
		// given
		AuthenticationDetailsGetter authenticationDetailsGetter = new AuthenticationDetailsGetter(userRepository);

		// when
		UserDetails userDetails = authenticationDetailsGetter.loadUserByUsername(email);

		// then exception is thrown
	}
}
