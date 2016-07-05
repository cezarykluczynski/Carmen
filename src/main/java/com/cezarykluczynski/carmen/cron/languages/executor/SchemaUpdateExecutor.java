package com.cezarykluczynski.carmen.cron.languages.executor;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.util.iterator.AnnotationIterator;
import com.cezarykluczynski.carmen.cron.languages.iterator.RefreshableTableIterator;
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory;
import com.cezarykluczynski.carmen.cron.languages.visitor.UpdaterVisitorComposite;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SchemaUpdateExecutor implements Runnable {

    private UpdaterVisitorComposite updaterVisitorComposite;

    private LanguagesIteratorsFactory languagesIteratorsFactory;

    @Autowired
    public SchemaUpdateExecutor(UpdaterVisitorComposite updaterVisitorComposite,
                                LanguagesIteratorsFactory languagesIteratorsFactory) {
        this.updaterVisitorComposite = updaterVisitorComposite;
        this.languagesIteratorsFactory = languagesIteratorsFactory;
    }

    @Override
    public void run() {
        AnnotationIterator annotationIterator = languagesIteratorsFactory.createLanguagesAnnotationIterator();
        Set<Class> visitedClasses = Sets.newHashSet();

        while(annotationIterator.hasNext()) {
            RefreshableTableIterator refreshableTableIterator = languagesIteratorsFactory
                    .createRefreshableTableIterator(annotationIterator.next());

            while(refreshableTableIterator.hasNext()) {
                RefreshableTable refreshableTable = refreshableTableIterator.next();
                Class refreshableTableBassClass = refreshableTable.getBaseClass();

                if (visitedClasses.contains(refreshableTableBassClass)) {
                    continue;
                }
                visitedClasses.add(refreshableTableBassClass);

                updaterVisitorComposite.visit(refreshableTable);
            }
        }
    }
}
