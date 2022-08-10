package in.neuw.left.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import in.neuw.left.clients.RightSideApiClient;
import in.neuw.left.models.LeftResponse;
import in.neuw.left.models.downstream.RightSideSignedRequest;
import in.neuw.left.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.ParseException;

/**
 * @author Karanbir Singh on 08/09/2022
 */
@Service
public class LeftService {

    @Value("${downstream.right.side.audience}")
    private String rightSideAudience;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RightSideApiClient rightSideApiClient;

    @Autowired
    private ObjectMapper objectMapper;

    public Mono<LeftResponse> getLeftSideData(final String correlationId) throws JOSEException {
        String token = jwtUtil.generateJWS(rightSideAudience, "right-side-signed", correlationId);
        RightSideSignedRequest request = new RightSideSignedRequest();
        request.setData(token);
        return rightSideApiClient.getRightSideResponse(request, correlationId).map(r -> {
            var response = new LeftResponse();
            var jwe = r.getData();
            try {
                var map = jwtUtil.decryptToken(jwe);
                response.setData(objectMapper.valueToTree(map));
            } catch (ParseException | JOSEException e) {
                throw new RuntimeException(e);
            }
            return response;
        });
    }

}
