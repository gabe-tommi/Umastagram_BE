package com.c11.umastagram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.RedirectView;

import com.c11.umastagram.controller.oAuthModels.OAuth2State;

import jakarta.servlet.http.HttpSession;
import java.util.Base64;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
public class OAuthController {

    private final RestTemplate restTemplate;

    public OAuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${spring.security.oauth2.client.registration.github-android.redirect-uri}")
    private String githubAndroidRedirectUri;

    @Value("${spring.security.oauth2.client.registration.github-android.client-id}")
    private String githubAndroidClientId;
    
    @Value("${spring.security.oauth2.client.registration.github-web.redirect-uri}")
    private String githubWebRedirectUri;

    @Value("${spring.security.oauth2.client.registration.github-web.client-id}")
    private String githubWebClientId;

    @Value("${spring.security.oauth2.client.registration.google-android.redirect-uri}")
    private String googleAndroidRedirectUri;

    @Value("${spring.security.oauth2.client.registration.google-android.client-id}")
    private String googleAndroidClientId;

    @Value("${spring.security.oauth2.client.registration.google-web.redirect-uri}")
    private String googleWebRedirectUri;

    @Value("${spring.security.oauth2.client.registration.google-web.client-id}")
    private String googleWebClientId;

    @Value("${spring.security.oauth2.client.registration.github-web.scope}")
    private String githubScope;

    @Value("${spring.security.oauth2.client.registration.google-web.scope}")
    private String googleScope;

    // to randomly generate state parameter for OAuth2
    private String generateState() {
        SecureRandom random = new SecureRandom();
        byte[] stateBytes = new byte[32]; // 256 bits of entropy
        random.nextBytes(stateBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(stateBytes);
    }

    private final Map<String, OAuth2State> stateStore = new ConcurrentHashMap<>();

    @GetMapping("/{provider}/{platform}")
    public RedirectView oauthLogin(@PathVariable String provider,
                                   @PathVariable String platform,
                                   HttpSession session) {
        
        String redirectUri = "";
        String clientId = "";
        String baseUrl = "";
        String scope = "";

         // Validate provider is supported
        if (platform.equals("android")) {

            if (provider.equals("github")) {
                baseUrl = "https://github.com/login/oauth/authorize?";
                clientId = githubAndroidClientId;
                redirectUri = githubAndroidRedirectUri;
                scope = githubScope;
            } else if (provider.equals("google")) {
                baseUrl = "https://accounts.google.com/o/oauth2/v2/auth?";
                clientId = googleAndroidClientId;
                redirectUri = googleAndroidRedirectUri;
                scope = googleScope;
            } else {
                throw new IllegalArgumentException("Unsupported provider: " + provider);
            }

        } else if (platform.equals("web")) {

            if (provider.equals("github")) {
                baseUrl = "https://github.com/login/oauth/authorize?";
                clientId = githubWebClientId;
                redirectUri = githubWebRedirectUri;
                scope = githubScope;
            } else if (provider.equals("google")) {
                baseUrl = "https://accounts.google.com/o/oauth2/v2/auth?";
                clientId = googleWebClientId;
                redirectUri = googleWebRedirectUri;
                scope = googleScope;
            } else {
                throw new IllegalArgumentException("Unsupported provider: " + provider);
            }

        } else {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }

        // Generate unique state for this request
        String state = generateState();
        OAuth2State stateData = new OAuth2State(provider, platform, System.currentTimeMillis());
        stateStore.put(state, stateData);
        session.setAttribute("oauth_state", state);
        session.setAttribute("oauth_provider", provider);
        session.setAttribute("oauth_platform", platform);

        String authUrl = baseUrl +
                "client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8) +
                "&response_type=code" +
                "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);

        return new RedirectView(authUrl);
    }

    private RedirectView handleValidationError(String message, HttpSession session) {
        // Clean up state from store and session
        String state = (String) session.getAttribute("oauth_state");
        if (state != null) {
            stateStore.remove(state);
        }
        session.removeAttribute("oauth_state");
        session.removeAttribute("oauth_provider");
        session.removeAttribute("oauth_platform");

        // Redirect to an error page or return an error response
        String errorRedirectUrl = "/error?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
        return new RedirectView(errorRedirectUrl);
    }

    @GetMapping("/{provider}/callback")
    public Object oauthCallback(@PathVariable String provider,
                                      String code,
                                      String state,
                                      HttpSession session) {
        // Implementation of callback handling goes here
        // This includes validating the state, exchanging the code for tokens,
        // retrieving user info, creating or fetching the user in the database,
        // generating a JWT token, and redirecting or responding with the token.
        OAuth2State stateData = stateStore.get(state);
        try{
            if (stateData == null || !stateData.getProvider().equals(provider) || stateData.isExpired(15 * 60 * 1000)) {
                throw new IllegalArgumentException("Invalid or expired state parameter");
            }
            if(session.getAttribute("oauth_state") == null || !session.getAttribute("oauth_state").equals(state)) {
                throw new IllegalArgumentException("State parameter does not match session");
            }
            if(session.getAttribute("oauth_provider") == null || !session.getAttribute("oauth_provider").equals(provider)) {
                throw new IllegalArgumentException("Provider does not match session");
            }
        } catch (IllegalArgumentException e) {
            return handleValidationError(e.getMessage(), session);
        }

        // Token Exchange and User Info Retrieval
        


        // For brevity, the full implementation is omitted here.
        return new RedirectView("/"); // Placeholder
    }

}
