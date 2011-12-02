/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.repositories;

import it.haslearnt.cassandra.CassandraColumnFamilies;
import it.haslearnt.model.User;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.junit.Test;
import org.scale7.cassandra.pelops.Mutator;
import org.scale7.cassandra.pelops.Selector;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import setup.IntegrationTest;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserRepositoryIntegrationTest extends IntegrationTest {
    @Resource(name = "userRepository")
    UserRepository userRepository;

    String email = "alelaska@wp.pl";
    String hashedPassword = "897y6euivbh";

    @Test
    public void shouldSaveUser() {
        //given
        User user = new User(email, hashedPassword);

        //when
        userRepository.save(user);

        //then
        Selector selector = pool.createSelector();
        List<Column> userColumns = selector.getColumnsFromRow(CassandraColumnFamilies.USERS, email, false, ConsistencyLevel.ONE);
        assertTrue(userColumns.size() == 1);
    }

    @Test
    public void saveShouldAlsoUpdate() {
        //given
        User user = new User(email, hashedPassword);
        String newPassword = "something completely different";

        //when
        userRepository.save(user);
        user.setHashedPassword(newPassword);
        userRepository.save(user);

        //then
        Selector selector = pool.createSelector();
        List<Column> userColumns = selector.getColumnsFromRow(CassandraColumnFamilies.USERS, email, false, ConsistencyLevel.ONE);
        assertTrue(userColumns.size() == 1);
    }

    @Test
    public void shouldFindByEmail() {
        //given
        saveUser();

        //when
        User user = userRepository.findByEmail(email);

        //then
        assertEquals(email, user.getEmail());
        assertEquals(hashedPassword, user.getHashedPassword());
    }

    private void saveUser() {
        Mutator mutator = pool.createMutator();
        mutator.writeColumns(CassandraColumnFamilies.USERS, email, mutator.newColumnList(
                mutator.newColumn(UserRepository.hashedPasswordColumn, hashedPassword)
            )
        );
        mutator.execute(ConsistencyLevel.ONE);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByEmailShouldThrowExceptionIfNotFound() {
        //when
        User user = userRepository.findByEmail(email);

        //then exceptions is thrown
    }
}
