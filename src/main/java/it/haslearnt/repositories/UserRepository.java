/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.repositories;

import com.google.common.annotations.VisibleForTesting;
import it.haslearnt.cassandra.CassandraColumnFamilies;
import it.haslearnt.model.User;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.scale7.cassandra.pelops.Mutator;
import org.scale7.cassandra.pelops.Selector;
import org.scale7.cassandra.pelops.pool.IThriftPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Utf8;

import java.util.List;

import static org.springframework.util.Assert.hasText;

public class UserRepository {
    @Autowired
    private IThriftPool pool;

    @VisibleForTesting
    static final String hashedPasswordColumn = "hashedPassword";

    public void save(User user) {
        Mutator mutator = pool.createMutator();
        mutator.writeColumns(CassandraColumnFamilies.USERS, user.getEmail(), mutator.newColumnList(
                mutator.newColumn(UserRepository.hashedPasswordColumn, user.getHashedPassword())
            )
        );
        mutator.execute(ConsistencyLevel.ONE);
    }

    public User findByEmail(String email) {
        hasText(email);
        Selector selector = pool.createSelector();
        List<Column> userColumns = selector.getColumnsFromRow(CassandraColumnFamilies.USERS, email, false, ConsistencyLevel.ONE);
        throwExceptionIfNotFound(userColumns, email);
        String hashedPassword = getHashedPassword(userColumns);
        return new User(email, hashedPassword);
    }

    private String getHashedPassword(List<Column> userColumns) {
        String hashedPassword = null;
        for(Column column : userColumns) {
            String propertyName = Utf8.decode(column.getName());
            String propertyValue = Utf8.decode(column.getValue());
            if(hashedPasswordColumn.equals(propertyName)) {
                hashedPassword = propertyValue;
            }
        }
        return hashedPassword;
    }

    private void throwExceptionIfNotFound(List<Column> columns, String email) {
        if (columns == null || columns.size() == 0) {
            throw new UsernameNotFoundException("User with email " + email + "  has not been found.");
        }
    }
}
