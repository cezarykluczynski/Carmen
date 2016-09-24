package com.cezarykluczynski.carmen.configuration;

import com.cezarykluczynski.carmen.client.github.GithubClient;
import com.cezarykluczynski.carmen.client.github.GithubEgitClient;
import com.cezarykluczynski.carmen.client.github.GithubJcabiClient;
import com.cezarykluczynski.carmen.client.github.GithubKohsukeClient;
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository.RateLimitRepository;
import com.jcabi.github.RtGithub;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GitHubClientsBeanConfiguration {

    @Autowired
    private ApplicationContext ctx;

    private String githubAccessToken;

    @Value("${github.httpClientUserAgentString}")
    private String githubHttpClientUserAgentString;

    public GitHubClientsBeanConfiguration() {
        /**
         * If enviroment variable CARMEN_GITHUB_ACCESS_TOKEN is present,
         * GitHub-related beans will be initialized using it. Otherwise, they will be initialized without
         * access token, resulting in much lower GitHub rate limits.
         */
        githubAccessToken = System.getenv("CARMEN_GITHUB_ACCESS_TOKEN");
    }

    @Bean
    public GithubClient githubClient() {
        return new GithubClient(
                (GithubJcabiClient) ctx.getBean("githubJcabiClient"),
                (GithubKohsukeClient) ctx.getBean("githubKohsukeClient"),
                (GithubEgitClient) ctx.getBean("githubEgitClient"),
                ctx.getBean(RateLimitRepository.class)
        );
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
                .header("User-Agent", githubHttpClientUserAgentString)
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
            System.out.println("Carmen: initializing org.eclipse.egit.github.core.client.GitHubClient " +
                    "without access token.");
        }

        githubClient.setUserAgent(githubHttpClientUserAgentString);

        return githubClient;
    }

}
