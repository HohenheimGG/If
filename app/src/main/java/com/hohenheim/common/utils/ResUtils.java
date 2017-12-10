package com.hohenheim.common.utils;

import java.lang.reflect.Field;

/**
 * Created by hohenheim on 2017/12/10.
 */

public class ResUtils {

    public static int getResId(String value, Class<?> clazz) {
        try {
            Field field = clazz.getDeclaredField(value);
            return field.getInt(field);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
