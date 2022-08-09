package in.neuw.left.config;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;

/**
 * @author Karanbir Singh on 08/09/2022
 */
@Configuration
public class JwksConfig {

    @Value("${data.private.jwks}")
    private String privateJwks;

    @Value("${data.public.jwks}")
    private String publicJwks;

    @Bean
    public JWKSet jwkSet() throws ParseException {
        JWKSet set = JWKSet.parse(privateJwks);
        return set;
    }

}
