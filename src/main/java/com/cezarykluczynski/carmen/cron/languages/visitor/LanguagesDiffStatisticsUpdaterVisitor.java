package com.cezarykluczynski.carmen.cron.languages.visitor;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor;
import com.cezarykluczynski.carmen.cron.languages.model.EntityField;
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO;
import com.cezarykluczynski.carmen.model.pub.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.SortedSet;

@Component
public class LanguagesDiffStatisticsUpdaterVisitor implements RefreshableTableVisitor {

    private LanguagesDAO languagesDAO;

    @Autowired
    public LanguagesDiffStatisticsUpdaterVisitor(LanguagesDAO languagesDAO) {
        this.languagesDAO = languagesDAO;
    }

    @Override
    public void visit(RefreshableTable refreshableTable) {
        SortedSet<EntityField> fields = refreshableTable.getFields();
        List<Language> languageList = languagesDAO.findAll();

        languageList.stream().forEach(language -> {
            fields.add(new EntityField("language_" + language.getId() + "_added"));
            fields.add(new EntityField("language_" + language.getId() + "_removed"));
        });

        refreshableTable.setFields(fields);
    }

}
