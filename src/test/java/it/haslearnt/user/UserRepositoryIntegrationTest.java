/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.user;

import static org.junit.Assert.*;

import org.junit.*;
import org.springframework.beans.factory.annotation.*;

import setup.*;

public class UserRepositoryIntegrationTest extends IntegrationTest {
	@Autowired
	UserRepository userRepository;

	String email = "alelaska@wp.pl";
	String hashedPassword = "897y6euivbh";

	@Test
	public void shouldSaveAndLoadUser() {
		User user = new User().withEmail(email).withPassword(hashedPassword);

		userRepository.save(user);

		User loadedUser = userRepository.load(user.email());
		assertNotNull(loadedUser);
		assertEquals(email, loadedUser.email());
		assertEquals(hashedPassword, loadedUser.password());
	}

	@Test
	public void saveShouldAlsoUpdate() {
		User user = new User().withEmail(email).withPassword(hashedPassword);
		userRepository.save(user);
		String oldId = user.email();

		userRepository.save(user.withPassword("NEW PASSWORD"));

		String newId = user.email();
		assertTrue(oldId == newId);
		assertEquals("NEW PASSWORD", userRepository.load(newId).password());
	}

	@Test
	public void findByIdShouldThrowExceptionIfNotFound() {
		User user = userRepository.load("NONEXISTENT ID");

		assertNull(user);
	}
}
