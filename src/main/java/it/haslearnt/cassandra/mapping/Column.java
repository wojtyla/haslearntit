package it.haslearnt.cassandra.mapping;

import java.lang.annotation.*;

/**
 * Denotes a field to be persisted in Cassandra.
 * 
 * @return
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

	/**
	 * If value not defined, the field name is used.
	 */
	String value() default "";

}
