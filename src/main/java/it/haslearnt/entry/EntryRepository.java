package it.haslearnt.entry;

import static org.scale7.cassandra.pelops.Selector.getColumnStringName;
import static org.scale7.cassandra.pelops.Selector.getColumnStringValue;
import it.haslearnt.cassandra.mappings.Id;

import java.lang.annotation.Annotation;
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

		Object id;
		List<Column> columns;
		try {
			id = idFromCassandraEntity(entry);
			columns = columnsFromCassandraEntity(entry, mutator);
		} catch (Exception e) {
			throw new RuntimeException("Problem with Cassandra mapping", e);
		}

		mutator.writeColumns("Entries", id.toString(), columns);

		mutator.execute(ConsistencyLevel.ONE);
	}

	private List<Column> columnsFromCassandraEntity(Entry entry, Mutator mutator) throws IllegalAccessException {
		List<Column> columns = new LinkedList<Column>();
		List<Field> result = getFieldsAnnotatedBy(it.haslearnt.cassandra.mapping.Column.class, Entry.class);
		for (Field field : result) {
			field.setAccessible(true);
			columns.add(
					mutator.newColumn(field.getAnnotation(it.haslearnt.cassandra.mapping.Column.class).value(), field.get(entry)
							.toString())
					);
		}
		return columns;
	}

	private List<Field> getFieldsAnnotatedBy(Class<? extends Annotation> annotation, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<Field> result = new LinkedList<Field>();
		for (Field field : fields) {
			if (field.isAnnotationPresent(annotation)) {
				result.add(field);
			}
		}
		return result;
	}

	private Object idFromCassandraEntity(Entry entry) throws IllegalAccessException {
		for (Field field : getFieldsAnnotatedBy(Id.class, entry.getClass())) {
			field.setAccessible(true);
			return field.get(entry);
		}
		throw new RuntimeException("Entity has no id");
	}

	public Entry fetchEntry(String entryId) {
		Selector selector = pool.createSelector();
		List<Column> columns = selector.getColumnsFromRow("Entries", entryId, true, ConsistencyLevel.ONE);

		Entry result = new Entry().withId(entryId);

		for (Column column : columns) {
			List<Field> entityColumns = getFieldsAnnotatedBy(it.haslearnt.cassandra.mapping.Column.class, Entry.class);

			for (Field field : entityColumns) {
				if (field.getAnnotation(it.haslearnt.cassandra.mapping.Column.class).value().equals(getColumnStringName(column)))
					try {
						field.setAccessible(true);
						field.set(result, getColumnStringValue(column));
					} catch (Exception e) {
						throw new RuntimeException("Entity mapping problem for Entry field: " + field.getName(), e);
					}
			}
		}

		return result;
	}

}
