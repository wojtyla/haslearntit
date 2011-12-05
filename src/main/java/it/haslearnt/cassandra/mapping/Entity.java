package it.haslearnt.cassandra.mapping;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {

	String value() default "";

}
