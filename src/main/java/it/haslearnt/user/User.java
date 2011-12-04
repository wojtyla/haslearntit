/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.user;

import it.haslearnt.cassandra.mapping.*;

@Entity("Users")
public class User {
	@Id
	private String email;
	@Column
	private String hashedPassword;

	public String email() {
		return email;
	}

	public String password() {
		return hashedPassword;
	}

	public User withEmail(String email) {
		this.email = email;
		return this;
	}

	public User withPassword(String password) {
		this.hashedPassword = password;
		return this;
	}
}