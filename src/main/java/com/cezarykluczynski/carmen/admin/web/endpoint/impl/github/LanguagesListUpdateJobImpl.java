package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github;

import com.cezarykluczynski.carmen.admin.web.endpoint.api.github.LanguagesListUpdateJob;
import com.cezarykluczynski.carmen.admin.web.endpoint.dto.LanguagesListUpdateStatusDTO;
import com.cezarykluczynski.carmen.cron.linguist.executor.LanguagesListUpdateExecutor;
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO;
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

    private LanguagesDAO languagesDAO;

    @Autowired
    public LanguagesListUpdateJobImpl(LanguagesListUpdateExecutor languagesListUpdateExecutor,
                                      LangsStatsAdapter langsStatsAdapter, LanguagesDAO languagesDAO) {
        this.languagesListUpdateExecutor = languagesListUpdateExecutor;
        this.langsStatsAdapter = langsStatsAdapter;
        this.languagesDAO = languagesDAO;
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
        Integer linguistLanguagesCount = langsStatsAdapter.getSupportedLanguages().size();
        Integer persistedLanguagesCount = languagesDAO.countAll();
        Boolean countsDiffers = !linguistLanguagesCount.equals(persistedLanguagesCount);

        if (updateWhenCountsDiffers && countsDiffers) {
            languagesListUpdateExecutor.run();
            persistedLanguagesCount = languagesDAO.countAll();
            countsDiffers = !linguistLanguagesCount.equals(persistedLanguagesCount);
        }

        return Response.ok().entity(LanguagesListUpdateStatusDTO.builder()
                .updatable(countsDiffers)
                .persistedLanguagesCount(persistedLanguagesCount)
                .linguistLanguagesCount(linguistLanguagesCount)
                .build()).build();
    }
}
