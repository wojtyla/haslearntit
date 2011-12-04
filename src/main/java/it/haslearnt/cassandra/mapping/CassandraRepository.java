package it.haslearnt.cassandra.mapping;

import static org.scale7.cassandra.pelops.Selector.getColumnStringName;
import static org.scale7.cassandra.pelops.Selector.getColumnStringValue;

import java.lang.reflect.*;
import java.util.*;

import org.apache.cassandra.thrift.*;
import org.apache.cassandra.thrift.Column;
import org.scale7.cassandra.pelops.*;
import org.scale7.cassandra.pelops.pool.*;
import org.springframework.beans.factory.annotation.*;

public class CassandraRepository<ENTITY> extends CassandraMapper {

	@Autowired
	protected IThriftPool pool;

	public void save(ENTITY entity) {
		if (entityId(entity) == null)
			generateIdOrThrowException(entity);

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

	private void generateIdOrThrowException(ENTITY entity) {
		try {
			entity.getClass().getMethod("generateId").invoke(entity);
		} catch (Exception e) {
			throw new RuntimeException("The entity of class " + entity.getClass().getName() + "(" + entity.toString()
					+ ") has no id assigned, and has no generateId method.");
		}
	}

	public ENTITY load(String entityId) {
		List<Column> entityColumns = fetchColumnsForId(entityId);
		if (entityColumns == null || entityColumns.isEmpty())
			return null;

		ENTITY result = instantiateENTITY();

		mapColumnsToFields(result, entityColumns);
		setId(result, entityId);

		return result;
	}

	private void setId(ENTITY entity, String entityId) {
		for (EntityField field : getFieldsAnnotatedBy(Id.class, entity.getClass()))
			field.setValueFor(entity, entityId);
	}

	private void mapColumnsToFields(ENTITY entity, List<Column> cassandraColumns) {
		for (Column column : cassandraColumns)
			mapColumnToMatchingEntityField(entity, column);
	}

	private void mapColumnToMatchingEntityField(ENTITY entity, Column column) {
		List<EntityField> entityColumns = getFieldsAnnotatedBy(it.haslearnt.cassandra.mapping.Column.class, entity.getClass());

		for (EntityField field : entityColumns)
			if (fieldIsAnnotatedWithThisColumn(column, field, entity))
				field.setValueFor(entity, getColumnStringValue(column));
	}

	private boolean fieldIsAnnotatedWithThisColumn(Column column, EntityField field, ENTITY entity) {
		String annotationValue = field.getAnnotationValue(it.haslearnt.cassandra.mapping.Column.class, entity);
		if ("".equals(annotationValue))
			annotationValue = field.name();

		return annotationValue.equals(getColumnStringName(column));
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