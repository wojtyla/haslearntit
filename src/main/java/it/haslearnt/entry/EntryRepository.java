package it.haslearnt.entry;

import static org.scale7.cassandra.pelops.Selector.getColumnStringName;
import static org.scale7.cassandra.pelops.Selector.getColumnStringValue;
import it.haslearnt.cassandra.mappings.Id;

import java.lang.reflect.Field;
import java.util.LinkedList;
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

		List<Column> columns = new LinkedList<Column>();
		Object id = null;
		try {
			Field[] fields = Entry.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(Id.class))
					id = field.get(entry);
				else if (field.isAnnotationPresent(it.haslearnt.cassandra.mapping.Column.class)) {
					columns.add(
							mutator.newColumn(field.getAnnotation(it.haslearnt.cassandra.mapping.Column.class).value(), field.get(entry)
									.toString())
							);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Problem with Cassandra mapping", e);
		}

		mutator.writeColumns("Entries", id.toString(), columns);

		mutator.execute(ConsistencyLevel.ONE);
	}

	public Entry fetchEntry(String entryId) {
		Selector selector = pool.createSelector();
		List<Column> columns = selector.getColumnsFromRow("Entries", entryId, true, ConsistencyLevel.ONE);

		Entry result = new Entry();

		for (Column column : columns) {
			if ("skill".equals(getColumnStringName(column)))
				result.iveLearnt(getColumnStringValue(column));
			else if ("when".equals(getColumnStringName(column)))
				result.today();
			else if ("difficulty".equals(getColumnStringName(column)))
				result.andItWas(getColumnStringValue(column));
		}

		return result;
	}

}
