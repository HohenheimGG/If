package com.hohenheim.processor;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by hohenheim on 17/10/28.
 */

class PermissionInfo {

    private Element elementUtils;
    private TypeElement classElement;

    private String packageName;
    private String proxyClassName;

    Map<Integer, String> grantMethodMap = new HashMap<>();
    Map<Integer, String> deniedMethodMap = new HashMap<>();
    Map<Integer, String> rationaleMethodMap = new HashMap<>();

    private static final String SUFFIC = "PermissionProxy";

    PermissionInfo(Elements elementUtils, TypeElement classElement) {
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        //classname
        String className = getClassName(classElement, packageName);
        this.packageName = packageName;
        this.proxyClassName = className + "$$" + SUFFIC;
    }

    private String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }

}
