/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.user;

import it.haslearnt.cassandra.mapping.CassandraEntity;
import it.haslearnt.cassandra.mapping.Column;
import it.haslearnt.cassandra.mapping.Entity;
import it.haslearnt.cassandra.mapping.Id;

@Entity("Users")
public class User extends CassandraEntity {
	@Id
	private String email;
	@Column("hashedPassword")
	private String hashedPassword;

	public User(String email, String hashedPassword) {
		this.email = email;
		this.hashedPassword = hashedPassword;
	}

	public String getEmail() {
		return email;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
}