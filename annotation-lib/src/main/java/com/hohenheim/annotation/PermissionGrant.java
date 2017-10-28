package com.hohenheim.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hohenheim on 17/10/28.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface PermissionGrant {
    int value();
}
