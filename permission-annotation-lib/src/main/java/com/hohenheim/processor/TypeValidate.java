package com.hohenheim.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by hohenheim on 17/10/28.
 */

class TypeValidate {

    static boolean isPublic(Element element) {
        return element.getModifiers().contains(PUBLIC);
    }

    static boolean isPrivate(Element element) {
        return element.getModifiers().contains(PRIVATE);
    }

    static boolean isAbstract(Element element) {
        return element.getModifiers().contains(ABSTRACT);
    }

    private boolean isMethod(Element element) {
        return element.getKind() == ElementKind.METHOD;
    }

    static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }
}
