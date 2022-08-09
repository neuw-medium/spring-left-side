package in.neuw.left.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Karanbir Singh on 08/09/2022
 */
@Configuration
public class DownstreamClientsConfig {

    @Value("${downstream.right.side.base}")
    public String basePathRightSide;

    @Bean
    public WebClient rightSideWebClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(basePathRightSide)
                .build();
        return webClient;
    }

}
