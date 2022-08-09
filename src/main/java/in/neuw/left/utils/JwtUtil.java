package in.neuw.left.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.ECPrivateKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.nimbusds.jose.JWSAlgorithm.*;

/**
 * @author Karanbir Singh on 08/09/2022
 */
@Component
public class JwtUtil {

    @Autowired
    private JWKSet jwkSet;

    @Value("${data.issuer}")
    private String issuer;

    public String generateJWS(final String audience,
                              final String subject) throws JOSEException {
        Optional<JWK> optionalJWK = jwkSet.getKeys()
                .stream()
                .filter(k -> {
                    return k.getKeyUse().equals(KeyUse.SIGNATURE) && k.getKeyType().equals(KeyType.EC);
                }).findAny();

        var key = optionalJWK.get();

        // technically key will always be there, but for runtime issues we may catch it, for now not doing it
        var header = new JWSHeader.Builder(ES256)
                .type(JOSEObjectType.JWT)
                .keyID(key.getKeyID())
                .build();

        var payload = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .audience(audience)
                .subject(subject)
                // the claim will be dynamic based on the case, you can set anything
                .claim("id", UUID.randomUUID().toString())
                .expirationTime(Date.from(Instant.now().plusSeconds(120)))
                .build();

        var signedJWT = new SignedJWT(header, payload);
        signedJWT.sign(new ECDSASigner((ECKey) key));
        return signedJWT.serialize();
    }

    // decrypt not verify(signature)
    public Map<String, Object> decryptToken(final String jweToken) throws ParseException, JOSEException {
        JWEObject jweObject = JWEObject.parse(jweToken);
        JWEHeader jweHeader = jweObject.getHeader();
        String kid = jweHeader.getKeyID();

        Optional<JWK> optionalJWK = jwkSet.getKeys()
                .stream()
                .filter(k -> {
                    return k.getKeyID().equals(kid);
                }).findAny();

        var key = optionalJWK.get();

        ECDHDecrypter decrypter = new ECDHDecrypter((ECPrivateKey) key);
        jweObject.decrypt(decrypter);

        var payload = jweObject.getPayload();
        return payload.toJSONObject();
    }

}
