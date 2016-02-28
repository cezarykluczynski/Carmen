package com.cezarykluczynski.carmen.cron.languages.visitor;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdaterVisitorComposite implements RefreshableTableVisitor {

    private EntityUpdaterVisitor entityUpdaterVisitor;

    private LanguagesDiffStatisticsUpdaterVisitor languagesDiffStatisticsUpdaterVisitor;

    private LanguagesStatisticsUpdaterVisitor languagesStatisticsUpdaterVisitor;

    private SchemaUpdaterVisitor schemaUpdaterVisitor;

    @Autowired
    public UpdaterVisitorComposite(EntityUpdaterVisitor entityUpdaterVisitor,
                                   LanguagesDiffStatisticsUpdaterVisitor languagesDiffStatisticsUpdaterVisitor,
                                   LanguagesStatisticsUpdaterVisitor languagesStatisticsUpdaterVisitor,
                                   SchemaUpdaterVisitor schemaUpdaterVisitor) {
        this.entityUpdaterVisitor = entityUpdaterVisitor;
        this.languagesDiffStatisticsUpdaterVisitor = languagesDiffStatisticsUpdaterVisitor;
        this.languagesStatisticsUpdaterVisitor = languagesStatisticsUpdaterVisitor;
        this.schemaUpdaterVisitor = schemaUpdaterVisitor;
    }

    @Override
    public void visit(RefreshableTable refreshableTable) {
        languagesDiffStatisticsUpdaterVisitor.visit(refreshableTable);
        languagesStatisticsUpdaterVisitor.visit(refreshableTable);
        schemaUpdaterVisitor.visit(refreshableTable);
        entityUpdaterVisitor.visit(refreshableTable);
    }

}
