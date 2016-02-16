package com.cezarykluczynski.carmen.cron.languages.iterator;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TableIterator implements Iterator<RefreshableTable> {

    private final Iterator<RefreshableTable> carmenNoSQLEntityIterator;

    public TableIterator() {
        Reflections reflections = new Reflections("com.cezarykluczynski.carmen");
        Set<Class<? extends RefreshableTable>> classes = reflections.getSubTypesOf(RefreshableTable.class);
        List<RefreshableTable> carmenNoSQLEntityList = new ArrayList<>();

        for(Class clazz : classes) {
            try {
                carmenNoSQLEntityList.add((RefreshableTable) clazz.getConstructor().newInstance());
            } catch(Throwable e) {
            }
        }

        carmenNoSQLEntityIterator = carmenNoSQLEntityList.iterator();
    }

    @Override
    public boolean hasNext() {
        return carmenNoSQLEntityIterator.hasNext();
    }

    @Override
    public RefreshableTable next() {
        return carmenNoSQLEntityIterator.next();
    }

}
