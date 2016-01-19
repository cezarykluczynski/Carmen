package com.cezarykluczynski.carmen.lang.stats.mapper;

import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat;
import com.cezarykluczynski.carmen.lang.stats.domain.LineStat;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface LanguageMapper {

    List<Language> mapLanguageList(JSONObject jsonObject);

    Map<Language, LineStat> mapRepositoryDescription(JSONObject jsonObject);

    Map<Language, LineDiffStat> mapCommitDescription(JSONObject jsonObject);

}
