package it.haslearnt.cassandra.mapping;

import java.lang.annotation.*;
import java.lang.reflect.*;

public class EntityField {

	private final Field field;

	public EntityField(Field field) {
		this.field = field;
	}

	public Object getValueFor(Object entity) {
		field.setAccessible(true);
		try {
			return field.get(entity);
		} catch (Exception e) {
			throw new RuntimeException("Problem with getting field " + field.getName() + " value for " + entity.getClass().getName(), e);
		}
	}

	public void setValueFor(Object entity, Object value) {
		field.setAccessible(true);
		try {
			field.set(entity, value);
		} catch (Exception e) {
			throw new RuntimeException("Problem with setting field " + field.getName() + " value for " + entity.getClass().getName(), e);
		}
	}

	public String getAnnotationValue(Class<? extends Annotation> annotationClass, Object entity) {
		Annotation annotation = field.getAnnotation(annotationClass);

		try {
			return (String) annotationClass.getMethod("value").invoke(annotation);
		} catch (Exception e) {
			throw new RuntimeException(
					"Cannot invoke 'value' on annotation " + annotationClass.getName() + ", maybe it has no 'value' method?", e);
		}
	}

	public String name() {
		return field.getName();
	}

}
