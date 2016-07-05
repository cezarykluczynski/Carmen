package com.cezarykluczynski.carmen.util.iterator;

import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

public class AnnotatedClassesIterator extends AbstractClassIterator {

    public AnnotatedClassesIterator(Class<? extends Annotation> annotation) {
        ClassPath classPath;

        try {
            classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        } catch(IOException e) {
            return;
        }

        Set<Class> classes = Sets.newHashSet();

        classPath.getAllClasses().stream()
                .filter(classInfo -> classInfo.getPackageName().startsWith("com.cezarykluczynski.carmen."))
                .forEach(className -> {
                    Class foundClass = null;
                    try {
                        foundClass = Class.forName(className.getName());
                    } catch(ClassNotFoundException e) {
                    }

                    if (foundClass != null && foundClass.isAnnotationPresent(annotation)) {
                        classes.add(foundClass);
                    }
                });

        iterator = classes.iterator();
    }

}
