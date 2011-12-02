/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.junit.Test;
import org.scale7.cassandra.pelops.Bytes;
import org.scale7.cassandra.pelops.Mutator;
import org.scale7.cassandra.pelops.Selector;

import setup.IntegrationTest;

public class EmbeddedCassandraTest extends IntegrationTest {
	@Test
	public void shouldConnectToEmbeddedCassandra() {
		// given
		Mutator mutator = pool.createMutator();
		Selector selector = pool.createSelector();

		// when
		mutator.writeColumn("Notes", "#1", mutator.newColumn(Bytes.fromLong(1), "test"));
		mutator.execute(ConsistencyLevel.ONE);

		// then
		Column column = selector.getColumnFromRow("Notes", "#1", Bytes.fromLong(1), ConsistencyLevel.ONE);
		assertNotNull(column);
		assertEquals("test", Bytes.toUTF8(column.value));
	}
}
