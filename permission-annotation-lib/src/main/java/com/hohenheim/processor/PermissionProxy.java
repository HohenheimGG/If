package com.hohenheim.processor;

/**
 * Created by hohenheim on 17/10/29.
 */

public interface PermissionProxy<T> {

    void grant(T source, int requestCode);

    void denied(T source, int requestCode);

    void rationale(T source, int requestCode);
}
