package in.co.geekninja.dbgen.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by PS on 2/24/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Coloumn {
    String coloumnName() default "";
    boolean Identity() default false;
    boolean autoIncrement() default false;
    boolean primaryKey() default false;
    boolean notNull() default false;
    String defaultValue() default "";

}

