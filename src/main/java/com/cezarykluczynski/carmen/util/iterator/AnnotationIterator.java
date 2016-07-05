package com.cezarykluczynski.carmen.util.iterator;

import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.*;

public class AnnotationIterator extends AbstractClassIterator implements Iterator<Class> {

    public AnnotationIterator(String packageName) {
        ClassPath classPath;

        try {
            classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        } catch(IOException e) {
            return;
        }

        Set<Class> classes = Sets.newHashSet();

        classPath.getTopLevelClasses(packageName).stream()
                .forEach(className -> {
                    try {
                        classes.add(Class.forName(className.getName()));
                    } catch(ClassNotFoundException e) {
                    }
                });

        iterator = classes.iterator();
    }

}
