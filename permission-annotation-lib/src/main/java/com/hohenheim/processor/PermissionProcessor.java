package com.hohenheim.processor;

/**
 * Created by hohenheim on 17/10/28.
 */

import com.hohenheim.annotation.PermissionDenied;
import com.hohenheim.annotation.PermissionGrant;
import com.hohenheim.annotation.ShowRequestPermissionRationale;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class PermissionProcessor extends AbstractProcessor{

    private Messager messager;
    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;//创建文件
    private Map<String, PermissionInfo> mPermissionMap = new HashMap<>();

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
        supportTypes.add(ShowRequestPermissionRationale.class.getCanonicalName());
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
        mPermissionMap.clear();
        return false;
    }

    private boolean processAnnotation(Class<? extends Annotation> clazz, RoundEnvironment roundEnv) {
        //遍历所有注解了clazz的元素
        for(Element element: roundEnv.getElementsAnnotatedWith(clazz)) {
            //判断被注解元素是否为方法
            if(!TypeValidate.isMethod(element))
                return false;
            //方法元素
            ExecutableElement methodElement = (ExecutableElement) element;
            //类元素
            TypeElement clazzElement = (TypeElement) methodElement.getEnclosingElement();
            //获取类名
            String className = clazzElement.getQualifiedName().toString();

            PermissionInfo info = mPermissionMap.get(className);
            if(info == null) {
                info = new PermissionInfo(this.elementUtils, clazzElement);
                mPermissionMap.put(className, info);
            }

            Annotation annotation = methodElement.getAnnotation(clazz);
            if(annotation instanceof PermissionDenied) {
                int requestCode = ((PermissionDenied) annotation).value();
                info.deniedMethodMap.put(requestCode, methodElement.getSimpleName().toString());
            } else if(annotation instanceof PermissionGrant) {
                int requestCode = ((PermissionGrant) annotation).value();
                info.grantMethodMap.put(requestCode, methodElement.getSimpleName().toString());
            } else if(annotation instanceof ShowRequestPermissionRationale) {
                int requestCode = ((ShowRequestPermissionRationale) annotation).value();
                info.rationaleMethodMap.put(requestCode, methodElement.getSimpleName().toString());
            } else {
                error(element, "%s not support .", clazz.getSimpleName());
                return false;
            }
        }
        return true;
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        this.messager.printMessage(Diagnostic.Kind.NOTE, message, element);
    }
}
