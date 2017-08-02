package com.jt.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * like spring Service
 * Created by he on 2017/8/1.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Managed {

    String value() default "";

    //map key
    String mapKey() default "";
}
