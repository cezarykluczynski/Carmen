package com.cezarykluczynski.carmen.cron.languages.api;

import com.cezarykluczynski.carmen.cron.languages.visitor.EntityUpdaterVisitor;
import com.cezarykluczynski.carmen.cron.languages.visitor.LanguagesDiffStatisticsUpdaterVisitor;
import com.cezarykluczynski.carmen.cron.languages.visitor.LanguagesStatisticsUpdaterVisitor;
import com.cezarykluczynski.carmen.cron.languages.visitor.SchemaUpdaterVisitor;

public interface RefreshableTableVisitor {

    RefreshableTableVisitor ENTITY_UPDATER_VISITOR = new EntityUpdaterVisitor();

    RefreshableTableVisitor SCHEMA_UPDATER_VISITOR = new SchemaUpdaterVisitor();

    RefreshableTableVisitor LANGUAGES_STATISTICS_UPDATER_VISITOR = new LanguagesStatisticsUpdaterVisitor();

    RefreshableTableVisitor LANGUAGES_DIFF_STATISTICS_UPDATER_VISITOR = new LanguagesDiffStatisticsUpdaterVisitor();

    void visit(RefreshableTable refreshableTable);

}
