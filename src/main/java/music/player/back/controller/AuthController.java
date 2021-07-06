package music.player.back.controller;

import lombok.RequiredArgsConstructor;
import music.player.back.dto.AccessAndRefreshTokenDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${app.redirect_uri}")
    String redirectUri;

    @Value("${app.client_id}")
    String clientId;

    @Value("${app.client_secret}")
    String clientSecret;

    @Value("${app.front_login_url}")
    String frontLoginUrl;


    @GetMapping("/callback")
    public RedirectView callback(@RequestParam String code) {
        MultiValueMap<String, String> bodyParamMap = new LinkedMultiValueMap<>();
        bodyParamMap.add("code", code);
        bodyParamMap.add("redirect_uri", redirectUri);
        bodyParamMap.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", codeBase64(clientId, clientSecret));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(bodyParamMap, headers);

        RestTemplate restTemplate = new RestTemplate();

        AccessAndRefreshTokenDTO tokens = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token",
                request,
                AccessAndRefreshTokenDTO.class).getBody();

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(frontLoginUrl + tokens.getAccess_token());
        return redirectView;
    }

    private String codeBase64(String client_id, String client_secret) {
        String auth = client_id + ":" + client_secret;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII) );
        return "Basic " + new String( encodedAuth );
    }
}
