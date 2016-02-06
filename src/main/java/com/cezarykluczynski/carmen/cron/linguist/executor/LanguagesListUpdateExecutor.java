package com.cezarykluczynski.carmen.cron.linguist.executor;

import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO;
import com.cezarykluczynski.carmen.dao.pub.enums.LinguistLanguageType;
import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter;
import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LanguagesListUpdateExecutor implements Runnable {

    @Autowired
    LangsStatsAdapter langsStatsAdapter;

    @Autowired
    LanguagesDAO languagesDAO;

    @Override
    public void run() {
        List<Language> linguistLanguageList = langsStatsAdapter.getSupportedLanguages();
        List<com.cezarykluczynski.carmen.model.pub.Language> initiallySavedLanguagesList = languagesDAO.findAll();
        List<Language> missingLinguistLanguageList = linguistLanguageList.stream()
            .filter(language -> !listHasLanguage(initiallySavedLanguagesList, language))
            .collect(Collectors.toList());

        List<com.cezarykluczynski.carmen.model.pub.Language> languagesListToSave = createLanguagesListToSave(missingLinguistLanguageList);
        languagesDAO.saveAll(languagesListToSave);

        List<com.cezarykluczynski.carmen.model.pub.Language> languagesWithParentsToSave = cc(linguistLanguageList);
        languagesDAO.saveAll(languagesWithParentsToSave);
    }

    private boolean listHasLanguage(List<com.cezarykluczynski.carmen.model.pub.Language> list, Language language) {
        return list.stream().filter(languageEntity -> languageEntity.getName().equals(language.getName())).collect(Collectors.toList()).size() > 0;
    }

    private List<com.cezarykluczynski.carmen.model.pub.Language> createLanguagesListToSave(List<Language> languagesList) {
        List<com.cezarykluczynski.carmen.model.pub.Language> languagesListToSave = new ArrayList<>();

        languagesList.stream().forEach(language -> {
            com.cezarykluczynski.carmen.model.pub.Language languageToSave = new com.cezarykluczynski.carmen.model.pub.Language();
            languageToSave.setLinguistColor(language.getColor());
            languageToSave.setLinguistLanguageType(LinguistLanguageType.valueOf(language.getType().toString()));
            languageToSave.setName(language.getName());
            languagesListToSave.add(languageToSave);
        });

        return languagesListToSave;
    }

    private List<com.cezarykluczynski.carmen.model.pub.Language> cc(List<Language> linguistLanguageList) {
        List<com.cezarykluczynski.carmen.model.pub.Language> savedLanguagesList = languagesDAO.findAll();
        List<com.cezarykluczynski.carmen.model.pub.Language> languagesWithParentsToSave = new ArrayList<>();

        for (com.cezarykluczynski.carmen.model.pub.Language savedLanguage : savedLanguagesList) {
            for (Language linguistLanguage : linguistLanguageList) {
                if (linguistLanguage.getName().equals(savedLanguage.getName()) && linguistLanguage.getParent() != null) {
                    for(com.cezarykluczynski.carmen.model.pub.Language savedLanguageParent : savedLanguagesList) {
                        if (savedLanguageParent.getName().equals(linguistLanguage.getParent().getName())) {
                            savedLanguage.setLinguistParent(savedLanguageParent);
                            languagesWithParentsToSave.add(savedLanguage);
                        }
                    }
                }
            }
        }

        return languagesWithParentsToSave;
    }
}
