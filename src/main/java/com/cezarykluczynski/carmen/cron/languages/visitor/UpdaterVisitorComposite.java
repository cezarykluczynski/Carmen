package com.cezarykluczynski.carmen.cron.languages.visitor;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor;
import org.springframework.stereotype.Component;

@Component
public class UpdaterVisitorComposite implements RefreshableTableVisitor {

    @Override
    public void visit(RefreshableTable refreshableTable) {
        RefreshableTableVisitor.LANGUAGES_DIFF_STATISTICS_UPDATER_VISITOR.visit(refreshableTable);
        RefreshableTableVisitor.LANGUAGES_STATISTICS_UPDATER_VISITOR.visit(refreshableTable);
        RefreshableTableVisitor.SCHEMA_UPDATER_VISITOR.visit(refreshableTable);
        RefreshableTableVisitor.ENTITY_UPDATER_VISITOR.visit(refreshableTable);
    }

}
