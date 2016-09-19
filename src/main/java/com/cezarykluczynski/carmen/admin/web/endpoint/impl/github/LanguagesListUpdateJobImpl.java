package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github;

import com.cezarykluczynski.carmen.admin.web.endpoint.api.github.LanguagesListUpdateJob;
import com.cezarykluczynski.carmen.admin.web.endpoint.dto.LanguagesListUpdateStatusDTO;
import com.cezarykluczynski.carmen.cron.linguist.executor.LanguagesListUpdateExecutor;
import com.cezarykluczynski.carmen.data.language.model.repository.LanguageRepository;
import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@Profile("admin-panel")
public class LanguagesListUpdateJobImpl implements LanguagesListUpdateJob {

    private LanguagesListUpdateExecutor languagesListUpdateExecutor;

    private LangsStatsAdapter langsStatsAdapter;

    private LanguageRepository languageRepository;

    @Autowired
    public LanguagesListUpdateJobImpl(LanguagesListUpdateExecutor languagesListUpdateExecutor,
                                      LangsStatsAdapter langsStatsAdapter, LanguageRepository languageRepository) {
        this.languagesListUpdateExecutor = languagesListUpdateExecutor;
        this.langsStatsAdapter = langsStatsAdapter;
        this.languageRepository = languageRepository;
    }

    @Override
    public Response getStatus() {
        return respond(false);
    }

    @Override
    public Response run() {
        return respond(true);
    }

    private Response respond(boolean updateWhenCountsDiffers) {
        Long persistedLanguagesCount = languageRepository.count();
        Integer linguistLanguagesCount = langsStatsAdapter.getSupportedLanguages().size();
        Boolean countsDiffers = Long.compare(linguistLanguagesCount.longValue(), persistedLanguagesCount) != 0;

        if (updateWhenCountsDiffers && countsDiffers) {
            languagesListUpdateExecutor.run();
            persistedLanguagesCount = languageRepository.count();
            countsDiffers = Long.compare(linguistLanguagesCount.longValue(), persistedLanguagesCount) != 0;
        }

        return Response.ok().entity(LanguagesListUpdateStatusDTO.builder()
                .updatable(countsDiffers)
                .persistedLanguagesCount(persistedLanguagesCount)
                .linguistLanguagesCount(new Long(linguistLanguagesCount))
                .build()).build();
    }
}
