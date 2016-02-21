package com.cezarykluczynski.carmen.cron.languages.visitor;

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable;
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor;
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO;
import com.cezarykluczynski.carmen.model.pub.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.SortedSet;

@Component
public class LanguagesStatisticsUpdaterVisitor implements RefreshableTableVisitor {

    private LanguagesDAO languagesDAO;

    @Autowired
    public LanguagesStatisticsUpdaterVisitor(LanguagesDAO languagesDAO) {
        this.languagesDAO = languagesDAO;
    }

    @Override
    public void visit(RefreshableTable refreshableTable) {
        SortedSet<String> fields = refreshableTable.getFields();
        List<Language> languageList = languagesDAO.findAll();

        languageList.stream().forEach(language -> {
            fields.add("language_" + language.getId());
        });

        refreshableTable.setFields(fields);
    }

}
