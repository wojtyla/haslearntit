/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import setup.IntegrationTest;

public class UserRepositoryIntegrationTest extends IntegrationTest {
	@Autowired
	UserRepository userRepository;

	String email = "alelaska@wp.pl";
	String hashedPassword = "897y6euivbh";

	@Test
	public void shouldSaveUser() {
		// given
		User user = new User(email, hashedPassword);

		// when
		userRepository.save(user);

		// then
		User loadedUser = userRepository.load(email);
		assertNotNull(loadedUser);
		assertEquals(email, loadedUser.getEmail());
		assertEquals(hashedPassword, loadedUser.getHashedPassword());
	}

	@Test
	public void saveShouldAlsoUpdate() {
		// given
		User user = new User(email, hashedPassword);
		String newPassword = "something completely different";

		// when
		userRepository.save(user);
		user.setHashedPassword(newPassword);
		userRepository.save(user);

		// then
		// assertEquals(1, userRepository.loadAll(email).size());
	}

	public void findByEmailShouldThrowExceptionIfNotFound() {
		User user = userRepository.load(email);

		assertNull(user);
	}
}
