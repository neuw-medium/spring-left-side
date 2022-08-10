package in.neuw.left.web.controllers;

import com.nimbusds.jose.JOSEException;
import in.neuw.left.models.LeftResponse;
import in.neuw.left.services.LeftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Karanbir Singh on 08/09/2022
 */
@RestController
@RequestMapping("/apis/v1/left")
public class LeftController {

    @Autowired
    private LeftService leftService;

    @GetMapping
    public Mono<LeftResponse> getData(final ServerWebExchange exchange) throws JOSEException {
        String correlationId = exchange.getAttribute("correlation-id");
        return leftService.getLeftSideData(correlationId);
    }

}
