package com.hohenheim.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by com.hohenheim on 17/10/28.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface ShowRequestPermissionRationale {
    int value();
}
