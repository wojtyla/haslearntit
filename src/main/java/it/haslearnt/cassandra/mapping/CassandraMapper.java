package it.haslearnt.cassandra.mapping;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.apache.cassandra.thrift.Column;
import org.scale7.cassandra.pelops.*;

public class CassandraMapper {

	protected List<Column> entityColumns(Object entity, Mutator mutator) throws IllegalAccessException {
		List<Column> columns = new LinkedList<Column>();
		List<EntityField> result = getFieldsAnnotatedBy(it.haslearnt.cassandra.mapping.Column.class, entity.getClass());
		for (EntityField field : result) {
			columns.add(
					mutator.newColumn(field.getAnnotationValue(it.haslearnt.cassandra.mapping.Column.class, entity),
							field.getValueFor(entity)
									.toString())
					);
		}
		return columns;
	}

	protected String entityId(Object entity) {
		for (EntityField field : getFieldsAnnotatedBy(Id.class, entity.getClass())) {
			return (String) field.getValueFor(entity);
		}
		throw new RuntimeException("Entity has no id");
	}

	@SuppressWarnings("unchecked")
	protected String entityName() {
		Class<? extends EntityWithGeneratedId> entityClass = (Class<? extends EntityWithGeneratedId>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		if (entityClass.isAnnotationPresent(Entity.class))
			return entityClass.getAnnotation(Entity.class).value();

		return entityClass.getSimpleName();
	}

	protected List<EntityField> getFieldsAnnotatedBy(Class<? extends Annotation> annotation, Class<?> clazz) {
		List<EntityField> result = new LinkedList<EntityField>();
		Class<?> currentClass = clazz;
		do {
			Field[] fields = currentClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(annotation)) {
					result.add(new EntityField(field));
				}
			}
			currentClass = currentClass.getSuperclass();
		} while (currentClass != null);

		return result;
	}
}