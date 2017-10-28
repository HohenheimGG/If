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
        this.classElement = classElement;
        PackageElement packageElement = elementUtils.getPackageOf(this.classElement);
        String packageName = packageElement.getQualifiedName().toString();
        //classname
        String className = getClassName(this.classElement, packageName);
        this.packageName = packageName;
        this.proxyClassName = className + "$$" + SUFFIC;
    }

    private String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }

    String generateCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").
                append(this.packageName).
                append(";\n\n");
        builder.append("import com.hohenheim.processor.*;\n\n");
        builder.append("public class").
                append(this.proxyClassName).
                append("implements ").
                append(SUFFIC).
                append("<").
                append(this.classElement.getSimpleName()).
                append(">").
                append("{\n");

        generateGrant(builder);
        generateDenied(builder);
        generateRational(builder);

        builder.append("\n}");
        return builder.toString();
    }

    void generateGrant(StringBuilder builder) {
        builder.append("@Override\n").
                append("public void grant(").
                append(classElement.getSimpleName()).
                append(" element, int requestCode) {\n");
        builder.append("switch(requestCode) {\n");

        for(int code: grantMethodMap.keySet()) {
            builder.append("case ").
                    append(code).
                    append(":\n").
                    append("element.").
                    append(grantMethodMap.get(code)).
                    append("();\n");
            builder.append("break;\n");
        }

        builder.append("}\n").
                append("}\n");
    }

    void generateDenied(StringBuilder builder) {
        builder.append("@Override\n").
                append("public void denied(").
                append(classElement.getSimpleName()).
                append(" element, int requestCode) {\n");
        builder.append("switch(request) {\n");

        for(int code: deniedMethodMap.keySet()) {
            builder.append("case ").
                    append(code).
                    append(":\n").
                    append("element.").
                    append(deniedMethodMap.get(code)).
                    append("();\n");
            builder.append("break;\n");
        }

        builder.append("}\n").
                append("}\n");
    }

    void generateRational(StringBuilder builder) {
        builder.append("@Override\n").
                append("public void rationale(").
                append(classElement.getSimpleName()).
                append(" element, int requestCode) {\n");
        builder.append("switch(request) {\n");

        for(int code: rationaleMethodMap.keySet()) {
            builder.append("case ").
                    append(code).
                    append(":\n").
                    append("element.").
                    append(rationaleMethodMap.get(code)).
                    append("();\n");
            builder.append("break;\n");
        }

        builder.append("}\n").
                append("}\n");
    }

    String getClassFullName() {
        return packageName + "." + proxyClassName;
    }

    TypeElement getClassElement() {
        return classElement;
    }

}
