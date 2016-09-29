package com.cezarykluczynski.carmen.client.github;

import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GithubKohsukeClient implements GithubClientInterface {

    private GitHub github;

    @Autowired
    public GithubKohsukeClient(GitHub github) {
        this.github = github;
    }

}
