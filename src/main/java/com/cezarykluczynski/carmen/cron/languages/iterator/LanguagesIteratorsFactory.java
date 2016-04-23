package com.cezarykluczynski.carmen.cron.languages.iterator;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;
import org.springframework.stereotype.Component;

@Component
public class LanguagesIteratorsFactory {

    public AnnotationIterator createAnnotationIterator() {
        return new AnnotationIterator();
    }

    public RefreshableTableIterator createRefreshableTableIterator(Class clazz) {
        return new RefreshableTableIterator(clazz, this);
    }

    public EntityFieldsIterator createEntityFieldsIterator(Class clazz, FieldsFilter fieldsFilter) {
        return new EntityFieldsIterator(clazz, fieldsFilter);
    }

}
