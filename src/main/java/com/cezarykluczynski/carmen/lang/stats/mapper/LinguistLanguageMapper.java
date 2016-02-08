package com.cezarykluczynski.carmen.lang.stats.mapper;

import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.cezarykluczynski.carmen.lang.stats.domain.LanguageType;
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat;
import com.cezarykluczynski.carmen.lang.stats.domain.LineStat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class LinguistLanguageMapper implements LanguageMapper {

    public List<Language> mapLanguageList(JSONObject jsonObject) {
        List<Language> languageList = buildListFromJSONObject(jsonObject);
        sortLanguageListAlphabetically(languageList);
        return languageList;
    }

    public Map<Language, LineStat> mapRepositoryDescription(JSONObject jsonObject) {
        Map<Language, LineStat> repositoryDescription = new HashMap<>();

        Iterator keys = jsonObject.keys();

        while(keys.hasNext()) {
            String languageName = (String) keys.next();
            Language language = mapNameAndLanguageDetailsToEntity(languageName, null);
            repositoryDescription.put(language, new LineStat(jsonObject.getInt(languageName)));
        }

        return repositoryDescription;
    }

    public Map<Language, LineDiffStat> mapCommitDescription(JSONObject jsonObject) {
        Map<Language, LineDiffStat> commitDescription = new HashMap<>();

        Iterator keys = jsonObject.keys();

        while(keys.hasNext()) {
            String languageName = (String) keys.next();
            Language language = mapNameAndLanguageDetailsToEntity(languageName, null);
            JSONObject lineDiff = (JSONObject) jsonObject.get(languageName);
            commitDescription.put(language, new LineDiffStat(lineDiff.getInt("added"), lineDiff.getInt("removed")));
        }

        return commitDescription;
    }

    private List<Language> buildListFromJSONObject(JSONObject jsonObject) {
        List<Language> languageList = new ArrayList<>();
        Map<Integer, String> languagesRequiringParents = new HashMap<>();

        Iterator keys = jsonObject.keys();

        while(keys.hasNext()) {
            String languageName = (String) keys.next();
            JSONObject languageDetails = (JSONObject) jsonObject.get(languageName);
            Language language = mapNameAndLanguageDetailsToEntity(languageName, languageDetails);
            try {
                String group = (String) languageDetails.get("group");
                languagesRequiringParents.put(languageList.size(), group);
            } catch (JSONException e) {
            }

            languageList.add(language);
        }

        addParents(languageList, languagesRequiringParents);

        return languageList;
    }

    private void sortLanguageListAlphabetically(List<Language> languageList) {
        languageList.sort(new Comparator<Language>() {

            public int compare(Language base, Language compare) {
                String baseName = base.getName();
                String compareName = compare.getName();

                return baseName.toLowerCase().compareTo(compareName.toLowerCase());
            }

        });
    }

    private void addParents(List<Language> languageList, Map<Integer, String> languagesRequiringParents) {
        Iterator iterator = languagesRequiringParents.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            String parentLanguageName = (String) entry.getValue();
            Language languageRequiringParent = languageList.get((Integer) entry.getKey());

            for (Language existingLanguage : languageList) {
                if (existingLanguage.getName().equals(parentLanguageName)) {
                    languageRequiringParent.setParent(existingLanguage);
                    languageRequiringParent.setColor(existingLanguage.getColor());
                    languageRequiringParent.setType(existingLanguage.getType());
                }
            }
        }
    }

    private Language mapNameAndLanguageDetailsToEntity(String languageName, JSONObject languageDetails) {
        Language language = new Language(languageName);

        if (languageDetails == null) {
            return language;
        }

        try {
            language.setColor((String) languageDetails.get("color"));
        } catch (JSONException e) {
        }

        try {
            language.setType(LanguageType.valueOf(((String) languageDetails.get("type")).toUpperCase()));
        } catch(JSONException | IllegalArgumentException e) {
        }

        return language;
    }

}
