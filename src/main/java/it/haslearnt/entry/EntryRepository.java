package it.haslearnt.entry;

import static org.scale7.cassandra.pelops.Selector.getColumnStringName;
import static org.scale7.cassandra.pelops.Selector.getColumnStringValue;

import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.scale7.cassandra.pelops.Mutator;
import org.scale7.cassandra.pelops.Selector;
import org.scale7.cassandra.pelops.pool.IThriftPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EntryRepository {

	@Autowired
	protected IThriftPool pool;

	public void save(Entry entry) {
		Mutator mutator = pool.createMutator();
		entry.generateId();
		mutator.writeColumns("Entries", entry.id(), mutator.newColumnList(
				mutator.newColumn("iveLearnt", entry.what()),
				mutator.newColumn("when", entry.when()),
				mutator.newColumn("difficulty", entry.howDifficult())));
		mutator.execute(ConsistencyLevel.ONE);
	}

	public Entry fetchEntry(String entryId) {
		Selector selector = pool.createSelector();
		List<Column> columns = selector.getColumnsFromRow("Entries", entryId, true, ConsistencyLevel.ONE);

		Entry result = new Entry();

		for (Column column : columns) {
			if ("iveLearnt".equals(getColumnStringName(column)))
				result.iveLearnt(getColumnStringValue(column));
			else if ("when".equals(getColumnStringName(column)))
				result.today();
			else if ("difficulty".equals(getColumnStringName(column)))
				result.andItWas(getColumnStringValue(column));
		}

		return result;
	}

}
