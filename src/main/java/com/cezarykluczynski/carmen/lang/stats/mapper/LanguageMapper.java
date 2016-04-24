package com.cezarykluczynski.carmen.lang.stats.mapper;

import com.cezarykluczynski.carmen.lang.stats.domain.*;
import org.json.JSONObject;

import java.util.List;

public interface LanguageMapper {

    List<Language> mapLanguageList(JSONObject jsonObject);

    String mapLinguistVersion(JSONObject jsonObject);

    RepositoryDescription toRepositoryDescription(String commitHash, JSONObject jsonObject);

    CommitDescription toCommitDescription(String commitHash, JSONObject jsonObject);

}
