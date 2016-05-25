package com.cezarykluczynski.carmen.dao.pub;

import com.cezarykluczynski.carmen.model.pub.Language;

import java.util.List;

public interface LanguagesDAO {

    List<Language> findAll();

    void saveAll(List<Language> languageList);

    Integer countAll();

}
