package com.cezarykluczynski.carmen.cron.languages.executor;

import com.cezarykluczynski.carmen.cron.languages.iterator.AnnotationIterator;
import com.cezarykluczynski.carmen.cron.languages.iterator.RefreshableTableIterator;
import com.cezarykluczynski.carmen.cron.languages.visitor.UpdaterVisitorComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchemaUpdateExecutor implements Runnable {

    private UpdaterVisitorComposite updaterVisitorComposite;

    @Autowired
    public SchemaUpdateExecutor(UpdaterVisitorComposite updaterVisitorComposite) {
        this.updaterVisitorComposite = updaterVisitorComposite;
    }

    @Override
    public void run() {
        AnnotationIterator annotationIterator = new AnnotationIterator();

        while(annotationIterator.hasNext()) {
            RefreshableTableIterator refreshableTableIterator = new RefreshableTableIterator(annotationIterator.next());

            while(refreshableTableIterator.hasNext()) {
                updaterVisitorComposite.visit(refreshableTableIterator.next());
            }
        }
    }
}
