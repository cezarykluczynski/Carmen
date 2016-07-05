package com.cezarykluczynski.carmen.util.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractClassIterator implements Iterator<Class> {

    protected Iterator<Class> iterator;

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
