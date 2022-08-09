package in.neuw.left.clients;

import in.neuw.left.models.downstream.RightSideResponse;
import in.neuw.left.models.downstream.RightSideSignedRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Karanbir Singh on 08/09/2022
 */
@Slf4j
@Service
public class RightSideApiClient {

    @Autowired
    private WebClient rightSideWebClient;

    @Value("${downstream.right.side.api}")
    private String rightSidePath;

    public Mono<RightSideResponse> getRightSideResponse(final RightSideSignedRequest rightSideSignedRequest) {
        return rightSideWebClient.post()
                .uri(rightSidePath)
                .body(Mono.just(rightSideSignedRequest), RightSideSignedRequest.class)
                .retrieve().bodyToMono(RightSideResponse.class);
    }

}
