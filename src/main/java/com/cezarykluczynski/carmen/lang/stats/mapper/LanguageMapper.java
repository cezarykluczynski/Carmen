package com.cezarykluczynski.carmen.lang.stats.mapper;

import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import org.json.JSONObject;

import java.util.List;

public interface LanguageMapper {

    List<Language> mapList(JSONObject jsonObject);

}
