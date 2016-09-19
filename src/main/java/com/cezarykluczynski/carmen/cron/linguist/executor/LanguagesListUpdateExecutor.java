package com.cezarykluczynski.carmen.cron.linguist.executor;

import com.cezarykluczynski.carmen.data.language.model.entity.enums.LinguistLanguageType;
import com.cezarykluczynski.carmen.data.language.model.repository.LanguageRepository;
import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter;
import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LanguagesListUpdateExecutor implements Runnable {

    private LangsStatsAdapter langsStatsAdapter;

    private LanguageRepository languageRepository;

    @Autowired
    public LanguagesListUpdateExecutor(LangsStatsAdapter langsStatsAdapter, LanguageRepository languageRepository) {
        this.langsStatsAdapter = langsStatsAdapter;
        this.languageRepository = languageRepository;
    }

    @Override
    public void run() {
        List<Language> linguistLanguageList = langsStatsAdapter.getSupportedLanguages();
        List<com.cezarykluczynski.carmen.data.language.model.entity.Language> initiallySavedLanguagesList =
                languageRepository.findAll();
        List<Language> missingLinguistLanguageList = linguistLanguageList.stream()
            .filter(language -> !listHasLanguage(initiallySavedLanguagesList, language))
            .collect(Collectors.toList());

        List<com.cezarykluczynski.carmen.data.language.model.entity.Language> languagesListToSave =
                createLanguagesListToSave(missingLinguistLanguageList);
        languageRepository.save(languagesListToSave);

        List<com.cezarykluczynski.carmen.data.language.model.entity.Language> languagesWithParentsToSave =
                getLanguagesWithParents(linguistLanguageList);
        languageRepository.save(languagesWithParentsToSave);
    }

    private boolean listHasLanguage(List<com.cezarykluczynski.carmen.data.language.model.entity.Language> list,
            Language language) {
        return list.stream()
                .filter(languageEntity -> languageEntity.getName().equals(language.getName()))
                .collect(Collectors.toList()).size() > 0;
    }

    private List<com.cezarykluczynski.carmen.data.language.model.entity.Language> createLanguagesListToSave(
            List<Language> languagesList) {
        List<com.cezarykluczynski.carmen.data.language.model.entity.Language> languagesListToSave = new ArrayList<>();

        languagesList.stream().forEach(language -> {
            com.cezarykluczynski.carmen.data.language.model.entity.Language languageToSave =
                    new com.cezarykluczynski.carmen.data.language.model.entity.Language();
            languageToSave.setLinguistColor(language.getColor());
            languageToSave.setLinguistLanguageType(LinguistLanguageType.valueOf(language.getType().toString()));
            languageToSave.setName(language.getName());
            languagesListToSave.add(languageToSave);
        });

        return languagesListToSave;
    }

    private List<com.cezarykluczynski.carmen.data.language.model.entity.Language> getLanguagesWithParents(
            List<Language> linguistLanguageList) {
        List<com.cezarykluczynski.carmen.data.language.model.entity.Language> savedLanguagesList =
                languageRepository.findAll();
        List<com.cezarykluczynski.carmen.data.language.model.entity.Language> languagesWithParentsToSave =
                Lists.newArrayList();

        for (com.cezarykluczynski.carmen.data.language.model.entity.Language savedLanguage : savedLanguagesList) {
            for (Language linguistLanguage : linguistLanguageList) {
                if (linguistLanguage.getName().equals(savedLanguage.getName()) &&
                        linguistLanguage.getParent() != null) {
                    for(com.cezarykluczynski.carmen.data.language.model.entity.Language savedLanguageParent :
                            savedLanguagesList) {
                        if (savedLanguageParent.getName().equals(linguistLanguage.getParent().getName())) {
                            if (!savedLanguage.getId().equals(savedLanguageParent.getId()) &&
                                    (savedLanguage.getLinguistParent() == null ||
                                    !savedLanguage.getLinguistParent().getName().equals(savedLanguageParent.getName())))
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
