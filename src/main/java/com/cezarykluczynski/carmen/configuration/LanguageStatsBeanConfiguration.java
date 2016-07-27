package com.cezarykluczynski.carmen.configuration;

import com.cezarykluczynski.carmen.lang.stats.adapter.CLILangsStatsAdapter;
import com.cezarykluczynski.carmen.lang.stats.adapter.HTTPLangsStatsAdapter;
import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter;
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper;
import com.cezarykluczynski.carmen.lang.stats.mapper.LinguistLanguageMapper;
import com.cezarykluczynski.carmen.util.network.HTTPClient;
import com.cezarykluczynski.carmen.util.network.HTTPJSONClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class LanguageStatsBeanConfiguration {

    @Value("${detector.client}")
    private String detectorClient;

    @Value("${detector.ip}")
    private String detectorIp;

    @Value("${detector.port}")
    private int detectorPort;

    @Bean
    public LangsStatsAdapter langStatsAdapter() throws IOException {
        LanguageMapper languageMapper = new LinguistLanguageMapper();

        if (detectorClient.equals("http")) {
            return new HTTPLangsStatsAdapter(getConfiguredHTTPClient(detectorIp, detectorPort), languageMapper);
        } else if (detectorClient.equals("cli")) {
            return new CLILangsStatsAdapter(languageMapper);
        } else {
            throw new IOException("Detector client could not be instantiated. Unknown client \"" + detectorClient +
                    "\".");
        }
    }

    @Bean
    public LanguageMapper languageMapper() {
        return new LinguistLanguageMapper();
    }

    private HTTPClient getConfiguredHTTPClient(String ip, int port) {
        return new HTTPJSONClientImpl(ip, port);
    }

}
