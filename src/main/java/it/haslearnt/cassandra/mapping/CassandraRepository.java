package it.haslearnt.cassandra.mapping;

import static org.scale7.cassandra.pelops.Selector.getColumnStringName;
import static org.scale7.cassandra.pelops.Selector.getColumnStringValue;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.scale7.cassandra.pelops.Mutator;
import org.scale7.cassandra.pelops.Selector;
import org.scale7.cassandra.pelops.pool.IThriftPool;
import org.springframework.beans.factory.annotation.Autowired;

public class CassandraRepository<ENTITY extends CassandraEntity> extends CassandraMapper {

	@Autowired
	protected IThriftPool pool;

	public void save(ENTITY entity) {
		if (entity.id() == null)
			entity.generateId();

		Mutator mutator = pool.createMutator();

		String id;
		List<Column> columns;
		try {
			id = entityId(entity);
			columns = entityColumns(entity, mutator);
		} catch (Exception e) {
			throw new RuntimeException("Problem with Cassandra mapping", e);
		}

		mutator.writeColumns(entityName(), id.toString(), columns);
		mutator.execute(ConsistencyLevel.ONE);
	}

	public ENTITY load(String entityId) {
		ENTITY result = instantiateENTITY();

		mapColumnsToFields(result, fetchColumnsForId(entityId));

		return result;
	}

	private void mapColumnsToFields(ENTITY result, List<Column> columns) {
		for (Column column : columns) {
			List<Field> entityColumns = getFieldsAnnotatedBy(it.haslearnt.cassandra.mapping.Column.class, result.getClass());

			for (Field field : entityColumns) {
				if (field.getAnnotation(it.haslearnt.cassandra.mapping.Column.class).value().equals(getColumnStringName(column)))
					try {
						field.setAccessible(true);
						field.set(result, getColumnStringValue(column));
					} catch (Exception e) {
						throw new RuntimeException("Entity mapping problem for " + result.getClass().getName() + " field: "
								+ field.getName(), e);
					}
			}
		}
	}

	private List<Column> fetchColumnsForId(String entityId) {
		Selector selector = pool.createSelector();
		List<Column> columns = selector.getColumnsFromRow(entityName(), entityId, true, ConsistencyLevel.ONE);
		return columns;
	}

	@SuppressWarnings("unchecked")
	private ENTITY instantiateENTITY() {
		try {
			return ((Class<ENTITY>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0])
					.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Problem instantiating class "
					+ ((Class<ENTITY>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName(), e);
		}
	}

}