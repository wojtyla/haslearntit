package it.haslearnt.cassandra.mapping;

import it.haslearnt.entry.Entry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import org.apache.cassandra.thrift.Column;
import org.scale7.cassandra.pelops.Mutator;

public class CassandraMapper {

	protected List<Column> entityColumns(CassandraEntity entry, Mutator mutator) throws IllegalAccessException {
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

	protected String entityId(CassandraEntity entry) throws IllegalAccessException {
		for (Field field : getFieldsAnnotatedBy(Id.class, entry.getClass())) {
			field.setAccessible(true);
			return (String) field.get(entry);
		}
		throw new RuntimeException("Entity has no id");
	}

	@SuppressWarnings("unchecked")
	protected String entityName() {
		Class<? extends CassandraEntity> entityClass = (Class<? extends CassandraEntity>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		if (entityClass.isAnnotationPresent(Entity.class))
			return entityClass.getAnnotation(Entity.class).value();

		return entityClass.getSimpleName();
	}

	protected List<Field> getFieldsAnnotatedBy(Class<? extends Annotation> annotation, Class<?> clazz) {
		List<Field> result = new LinkedList<Field>();
		Class<?> currentClass = clazz;
		do {
			Field[] fields = currentClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(annotation)) {
					result.add(field);
				}
			}
			currentClass = currentClass.getSuperclass();
		} while (currentClass != null);

		return result;
	}
}