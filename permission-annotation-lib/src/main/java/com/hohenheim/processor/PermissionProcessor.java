package com.hohenheim.processor;

/**
 * Created by hohenheim on 17/10/28.
 */

import com.hohenheim.annotation.PermissionDenied;
import com.hohenheim.annotation.PermissionGrant;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class PermissionProcessor extends AbstractProcessor{

    private Messager messager;
    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;//创建文件

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    /**
     * 注解处理器处理的注解
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new HashSet<>();
        supportTypes.add(PermissionDenied.class.getCanonicalName());
        supportTypes.add(PermissionGrant.class.getCanonicalName());
        return supportTypes;
    }

    /**
     * 指定java版本
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }


}
