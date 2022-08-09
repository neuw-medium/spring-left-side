package in.neuw.left.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * @author Karanbir Singh on 08/09/2022
 */
@RestController
@RequestMapping("/.well-known")
public class WellKnownController {

    @Value("${data.public.jwks}")
    private String publicJWKS;

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin(origins = {"https://jwt.io"})
    @GetMapping("/openid-configuration")
    public Mono<JsonNode> getOpenIdConfig() {
        var map = new HashMap<>();
        map.put("jwks_uri", "http://localhost:30011/.well-known/jwks");
        return Mono.just(objectMapper.convertValue(map, JsonNode.class));
    }

    @CrossOrigin(origins = {"https://jwt.io"})
    @GetMapping("/jwks")
    public Mono<JsonNode> getJwks() throws JsonProcessingException {
        return Mono.just(objectMapper.readTree(publicJWKS));
    }

}
