/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.user;

import it.haslearnt.cassandra.mapping.CassandraRepository;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends CassandraRepository<User> {
}
