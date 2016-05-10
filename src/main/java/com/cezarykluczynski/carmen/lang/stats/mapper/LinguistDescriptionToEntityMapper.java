package com.cezarykluczynski.carmen.lang.stats.mapper;

import com.cezarykluczynski.carmen.cron.languages.util.FieldNameUtil;
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO;
import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription;
import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat;
import com.cezarykluczynski.carmen.model.cassandra.GitDescription;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Component
public class LinguistDescriptionToEntityMapper {

    private LanguagesDAO languagesDAO;

    @Autowired
    public LinguistDescriptionToEntityMapper(LanguagesDAO languagesDAO) {
        this.languagesDAO = languagesDAO;
    }

    public GitDescription updateCommitUsingCommitDescription(GitDescription gitDescription,
                                                     CommitDescription commitDescription) {
        Map<Long, com.cezarykluczynski.carmen.model.pub.Language> map = listToLanguagesMap(languagesDAO.findAll());
        Map<Language, LineDiffStat> lineDiffStatMap = commitDescription.getLineDiffStats();

        List<Field> fields = Lists.newArrayList(gitDescription.getClass().getFields());
        fields.stream().filter(FieldNameUtil::isLanguageAddedOrRemoved)
                .forEach(field -> {
                    try {
                        Long languageId = Long.valueOf(field.getName().substring(9).split("_")[0]);
                        boolean isAdded = FieldNameUtil.isLanguageAdded(field);

                        if (map.containsKey(languageId)) {
                            LineDiffStat lineDiffStat = lineDiffStatMap
                                    .get(new Language(map.get(languageId).getName()));
                            if (isAdded) {
                                field.set(gitDescription, lineDiffStat.getAddedLines());
                            } else {
                                field.set(gitDescription, lineDiffStat.getRemovedLines());
                            }
                        } else {
                            field.set(gitDescription, 0);
                        }
                    } catch (IllegalAccessException e) {
                    }
                });

        return gitDescription;
    }

    private Map<Long, com.cezarykluczynski.carmen.model.pub.Language> listToLanguagesMap(
            List<com.cezarykluczynski.carmen.model.pub.Language> list) {
        Map<Long, com.cezarykluczynski.carmen.model.pub.Language> map = Maps.newHashMap();
        list.stream().forEach(language -> map.put(language.getId(), language));
        return map;
    }

}
