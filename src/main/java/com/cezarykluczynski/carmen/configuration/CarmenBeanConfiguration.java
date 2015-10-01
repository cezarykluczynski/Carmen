package com.cezarykluczynski.carmen.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

import com.jcabi.github.RtGithub;

import org.kohsuke.github.GitHub;

import org.eclipse.egit.github.core.client.GitHubClient;

@Configuration
@ComponentScan
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
public class CarmenBeanConfiguration {

    private String githubAccessToken;

    @Value("${github.httpClientUserAgentString}")
    private String userAgentString;

    public CarmenBeanConfiguration() {
        /**
         * If enviroment variable CARMEN_GITHUB_ACCESS_TOKEN is present,
         * GitHub-related beans will be initialized using it. Otherwise, they will be initialized without
         * access token, resulting in much lower GitHub rate limits.
         */
        githubAccessToken = System.getenv("CARMEN_GITHUB_ACCESS_TOKEN");
    }

    @Bean
    public RtGithub rtGithub() {
        RtGithub rtGithub;

        if (githubAccessToken == null) {
            System.out.println("Carmen: initializing com.jcabi.github.RtGithub without access token.");
            rtGithub = new RtGithub();
        } else {
            rtGithub = new RtGithub(githubAccessToken);
        }

        rtGithub = new RtGithub(
            rtGithub.entry()
                .reset("User-Agent")
                .header("User-Agent", userAgentString)
        );

        return rtGithub;
    }

    @Bean
    public GitHub kohsukeGithubApi() throws IOException {
        if (githubAccessToken == null) {
            System.out.println("Carmen: initializing org.kohsuke.github.GitHub without access token.");
            return GitHub.connectAnonymously();
        }

        return GitHub.connectUsingOAuth(githubAccessToken);
    }

    @Bean
    public GitHubClient egitGithubClient() throws IOException {
        GitHubClient githubClient = new GitHubClient();

        if (githubAccessToken != null) {
            githubClient.setOAuth2Token(githubAccessToken);
        } else {
            System.out.println("Carmen: initializing org.eclipse.egit.github.core.client.GitHubClient without access token.");
        }

        githubClient.setUserAgent(userAgentString);

        return githubClient;
    }

}
