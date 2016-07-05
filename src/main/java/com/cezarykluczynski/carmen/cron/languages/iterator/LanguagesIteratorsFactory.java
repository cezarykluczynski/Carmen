package com.cezarykluczynski.carmen.cron.languages.iterator;

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter;
import org.springframework.stereotype.Component;

@Component
public class LanguagesIteratorsFactory {

    public LanguagesAnnotationIterator createLanguagesAnnotationIterator() {
        return new LanguagesAnnotationIterator();
    }

    public RefreshableTableIterator createRefreshableTableIterator(Class clazz) {
        return new RefreshableTableIterator(clazz, this);
    }

    public EntityFieldsIterator createEntityFieldsIterator(Class clazz, FieldsFilter fieldsFilter) {
        return new EntityFieldsIterator(clazz, fieldsFilter);
    }

}
