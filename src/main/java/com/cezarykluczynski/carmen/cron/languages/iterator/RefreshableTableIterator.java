package com.cezarykluczynski.carmen.cron.languages.iterator;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.model.RefreshableTableImpl;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RefreshableTableIterator implements Iterator<RefreshableTable> {

    private final Iterator<RefreshableTable> iterator;

    public RefreshableTableIterator(Class clazz) {
        Set<Class<?>> classes = new Reflections("com.cezarykluczynski.carmen").getTypesAnnotatedWith(clazz);
        List<RefreshableTable> refreshableTableList = new ArrayList<>();
        classes.forEach(refreshableClass -> refreshableTableList.add(new RefreshableTableImpl(refreshableClass)));
        iterator = refreshableTableList.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public RefreshableTable next() {
        return iterator.next();
    }

}
