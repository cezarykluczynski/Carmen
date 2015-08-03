package carmen.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.ComponentScan;

import com.jcabi.github.RtGithub;

@Configuration
@ComponentScan
public class CarmenBeanConfiguration {

    /**
     * If enviroment variable CARMEN_GITHUB_ACCESS_TOKEN is present,
     * should be initialized using it. Otherwise, RtGithub will be initialized without
     * access token, resulting in much lower GitHub rate limits.
     */
    @Bean
    public RtGithub rtGithub() {
        String token = System.getenv("CARMEN_GITHUB_ACCESS_TOKEN");

        if (token == null) {
            System.out.println("Carmen: initializing com.jcabi.github without access token.");
            return new RtGithub();
        }

        return new RtGithub(token);
    }
}
