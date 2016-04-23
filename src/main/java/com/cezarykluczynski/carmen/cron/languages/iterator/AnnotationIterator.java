package com.cezarykluczynski.carmen.cron.languages.iterator;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.*;

public class AnnotationIterator implements Iterator<Class> {

    private Iterator<Class> iterator;

    AnnotationIterator() {
        ClassPath classPath;

        try {
            classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        } catch(IOException e) {
            return;
        }

        List<Class> classes = new ArrayList<>();

        classPath.getTopLevelClasses("com.cezarykluczynski.carmen.cron.languages.annotations").stream()
                .forEach(className -> {
                    try {
                        classes.add(Class.forName(className.getName()));
                    } catch(ClassNotFoundException e) {
                    }
                });

        iterator = classes.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }

    @Override
    public Class next() {
        if (iterator != null) {
            return iterator.next();
        } else {
            throw new NoSuchElementException();
        }
    }
}
