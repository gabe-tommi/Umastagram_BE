package com.c11.umastagram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.security.Key;

import com.c11.umastagram.controller.oAuthModels.OAuth2State;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.c11.umastagram.model.User;
import com.c11.umastagram.service.UserService;

import jakarta.servlet.http.HttpSession;
import java.util.Base64;
import java.util.HashMap;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
public class OAuthController {

    private final RestTemplate restTemplate;
    private final UserService userService;

    public OAuthController(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    @Value("${GITHUB_CLIENT_REDIRECT_URI_ANDROID}")
    private String githubAndroidRedirectUri;

    @Value("${GITHUB_CLIENT_ID_ANDROID}")
    private String githubAndroidClientId;
    
    @Value("${GITHUB_CLIENT_REDIRECT_URI_WEB}")
    private String githubWebRedirectUri;

    @Value("${GITHUB_CLIENT_ID_WEB}")
    private String githubWebClientId;

    @Value("${GOOGLE_CLIENT_REDIRECT_URI_ANDROID}")
    private String googleAndroidRedirectUri;

    @Value("${GOOGLE_CLIENT_ID_ANDROID}")
    private String googleAndroidClientId;

    @Value("${GOOGLE_CLIENT_REDIRECT_URI_WEB}")
    private String googleWebRedirectUri;

    @Value("${GOOGLE_CLIENT_ID_WEB}")
    private String googleWebClientId;

    @Value("read:user,user:email")
    private String githubScope;

    @Value("openid profile email")
    private String googleScope;

    @Value("${GITHUB_CLIENT_SECRET_WEB}")
    private String githubClientSecret;

    @Value("${GOOGLE_CLIENT_SECRET_WEB}")
    private String googleClientSecret;

    @Value("${GITHUB_CLIENT_SECRET_ANDROID}")
    private String githubAndroidClientSecret;

    @Value("${GOOGLE_CLIENT_SECRET_ANDROID:}")
    private String googleAndroidClientSecret;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${app.web.success-redirect-url:https://feuma-b63c05ecb1df.herokuapp.com/tabs/posts}")
    private String webSuccessRedirectUrl;

    // to randomly generate state parameter for OAuth2
    private String generateState() {
        SecureRandom random = new SecureRandom();
        byte[] stateBytes = new byte[32]; // 256 bits of entropy
        random.nextBytes(stateBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(stateBytes);
    }

    // PKCE: Generate a random code_verifier (43-128 characters, base64url-encoded)
    private String generateCodeVerifier() {
        SecureRandom random = new SecureRandom();
        byte[] verifierBytes = new byte[64]; // 64 bytes = 512 bits → ~86 base64url chars
        random.nextBytes(verifierBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(verifierBytes);
    }

    // PKCE: Create code_challenge = BASE64URL(SHA256(code_verifier))
    private String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
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
        
        // PKCE: Generate code_verifier and code_challenge for mobile platforms
        String codeVerifier = null;
        String codeChallenge = null;
        
        if (platform.equals("android") && provider.equals("google")) {
            codeVerifier = generateCodeVerifier();
            codeChallenge = generateCodeChallenge(codeVerifier);
        }
        
        OAuth2State stateData = new OAuth2State(provider, platform, System.currentTimeMillis(), codeVerifier);
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
        
        // Add PKCE parameters for Android + Google
        if (codeChallenge != null) {
            authUrl += "&code_challenge=" + URLEncoder.encode(codeChallenge, StandardCharsets.UTF_8) +
                       "&code_challenge_method=S256";
        }

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

    private Map<String, String> exchangeCodeForToken(String provider, String code, String redirectUri, String clientId, String clientSecret, String codeVerifier) {
        // Determine token endpoint URL
        String tokenUrl;
        if ("github".equals(provider)) {
            tokenUrl = "https://github.com/login/oauth/access_token";
        } else if ("google".equals(provider)) {
            tokenUrl = "https://oauth2.googleapis.com/token";
        } else {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Prepare form data
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", code);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("client_id", clientId);
        
        // Add client_secret only if present (not needed for PKCE flows)
        if (clientSecret != null && !clientSecret.isEmpty()) {
            requestBody.add("client_secret", clientSecret);
        }
        
        // Add code_verifier for PKCE flows
        if (codeVerifier != null && !codeVerifier.isEmpty()) {
            requestBody.add("code_verifier", codeVerifier);
        }

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make POST request
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, requestEntity, String.class);
        
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Token exchange failed: " + response.getStatusCode());
        }

        // Parse response based on provider
        String responseBody = response.getBody();
        Map<String, String> tokenData = "github".equals(provider) ? 
        parseFormEncoded(responseBody) : 
        parseJsonTokenResponse(responseBody);
    
        if (tokenData.containsKey("error")) {
            String error = tokenData.get("error");
            String description = tokenData.get("error_description");
            throw new RuntimeException("OAuth2 error: " + error + " - " + description);
        }
         
        return tokenData;
    }

    private Map<String, String> parseFormEncoded(String responseBody) {
        Map<String, String> result = new HashMap<>();
        
        // Split by & to get key=value pairs
        String[] pairs = responseBody.split("&");
        
        for (String pair : pairs) {
            // Split each pair by = (but only once, in case value contains =)
            String[] keyValue = pair.split("=", 2); // limit=2 prevents splitting values with =
            
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                
                // URL decode the value (GitHub URL-encodes some characters)
                try {
                    value = URLDecoder.decode(value, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    // If decoding fails, use original value
                }
                
                result.put(key, value);
            }
        }
    
        return result;
    }

    private Map<String, String> parseJsonTokenResponse(String responseBody) {
        Map<String, String> result = new HashMap<>();
        
        try {
            // Create ObjectMapper for JSON parsing
            ObjectMapper objectMapper = new ObjectMapper();
            
            // Parse the entire JSON string into a JsonNode tree
            JsonNode rootNode = objectMapper.readTree(responseBody);
            
            // Extract required fields
            if (rootNode.has("access_token")) {
                result.put("access_token", rootNode.get("access_token").asText());
            }
            
            if (rootNode.has("token_type")) {
                result.put("token_type", rootNode.get("token_type").asText());
            }
            
            if (rootNode.has("scope")) {
                result.put("scope", rootNode.get("scope").asText());
            }
            
            // Optional fields
            if (rootNode.has("expires_in")) {
                result.put("expires_in", String.valueOf(rootNode.get("expires_in").asInt()));
            }
            
            if (rootNode.has("refresh_token")) {
                result.put("refresh_token", rootNode.get("refresh_token").asText());
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON token response: " + e.getMessage(), e);
        }
        
        return result;
    }

    private Map<String, Object> fetchUserInfo(String provider, String accessToken) {
        ObjectMapper objectMapper = new ObjectMapper();
        String userInfoUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        if ("github".equals(provider)) {
            userInfoUrl = "https://api.github.com/user";
            headers.set("User-Agent", "Umastagram");
        } else if ("google".equals(provider)) {
            userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        } else {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to fetch user info: " + response.getStatusCode());
        }

        JsonNode userJson; 
        try{
            userJson = objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse user info response: " + e.getMessage(), e); 
        }
        Map<String, Object> userInfo = new HashMap<>();

        if ("github".equals(provider)) {
            userInfo.put("id", userJson.has("id") ? userJson.get("id").asText() : null);
            userInfo.put("username", userJson.has("login") ? userJson.get("login").asText() : null);
            userInfo.put("name", userJson.has("name") && !userJson.get("name").isNull() ? userJson.get("name").asText() : null);
            userInfo.put("email", userJson.has("email") && !userJson.get("email").isNull() ? userJson.get("email").asText() : null);
            userInfo.put("avatar_url", userJson.has("avatar_url") ? userJson.get("avatar_url").asText() : null);
            
        } else if ("google".equals(provider)) {
            userInfo.put("id", userJson.has("id") ? userJson.get("id").asText() : null);
            userInfo.put("username", userJson.has("email") ? userJson.get("email").asText() : null);
            userInfo.put("name", userJson.has("name") && !userJson.get("name").isNull() ? userJson.get("name").asText() : null);
            userInfo.put("email", userJson.has("email") ? userJson.get("email").asText() : null);
            userInfo.put("avatar_url", userJson.has("picture") && !userJson.get("picture").isNull() ? userJson.get("picture").asText() : null);
        }

        return userInfo;
    }

    @GetMapping("/{provider}/callback")
    public Object oauthCallback(@PathVariable String provider,
                                      String code,
                                      String state,
                                      HttpSession session) {
        // LOG IMMEDIATELY - before any processing
        System.out.println("=== OAUTH CALLBACK HIT ===");
        System.out.println("Provider: " + provider);
        System.out.println("Code present: " + (code != null));
        System.out.println("State present: " + (state != null));
        System.out.println("Session present: " + (session != null));
        
        System.out.println("=== OAUTH CALLBACK STARTED ===");
        System.out.println("Provider: " + provider + ", Code: " + (code != null ? code.substring(0, 10) + "..." : "null") + ", State: " + state);
        
        // Implementation of callback handling goes here
        // This includes validating the state, exchanging the code for tokens,
        // retrieving user info, creating or fetching the user in the database,
        // generating a JWT token, and redirecting or responding with the token.
        
        // Validate state - check session first, then global store as fallback
        String sessionState = (String) session.getAttribute("oauth_state");
        String sessionProvider = (String) session.getAttribute("oauth_provider");
        String sessionPlatform = (String) session.getAttribute("oauth_platform");
        
        System.out.println("Session state: " + sessionState + ", Provider: " + sessionProvider + ", Platform: " + sessionPlatform);
        
        // Declare codeVerifier outside try block so it's accessible later
        String codeVerifier = null;
        
        try{
            // Primary validation: check if state matches session
            if (sessionState == null || !sessionState.equals(state)) {
                System.out.println("State parameter does not match session");
                throw new IllegalArgumentException("State parameter does not match session");
            }
            if (sessionProvider == null || !sessionProvider.equals(provider)) {
                System.out.println("Provider does not match session");
                throw new IllegalArgumentException("Provider does not match session");
            }
            
            // Secondary validation: check global store if available
            OAuth2State stateData = stateStore.get(state);
            if (stateData != null && (!stateData.getProvider().equals(provider) || stateData.isExpired(15 * 60 * 1000))) {
                System.out.println("State data validation failed");
                throw new IllegalArgumentException("Invalid or expired state parameter");
            }
            
            // If no stateData in global store, reconstruct from session
            if (stateData == null && sessionPlatform != null) {
                stateData = new OAuth2State(sessionProvider, sessionPlatform, System.currentTimeMillis(), null);
                System.out.println("Reconstructed state data from session: " + stateData);
            } else if (stateData == null) {
                System.out.println("No state data available");
                throw new IllegalArgumentException("State data not available");
            }
            
            // Extract code_verifier if present (for PKCE flows)
            codeVerifier = stateData.getCodeVerifier();
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
            return handleValidationError(e.getMessage(), session);
        }

        // Clean up state after successful authentication
        stateStore.remove(state);
        session.removeAttribute("oauth_state");
        session.removeAttribute("oauth_provider");
        session.removeAttribute("oauth_platform");

        // Platform-specific response handling
        String platform = sessionPlatform; // Use platform from session
        System.out.println("Using platform from session: " + platform);
        
        // Proceed with server-side token exchange for all platforms/providers
        System.out.println("Proceeding with server-side token exchange for platform: " + platform);
        Map<String, String> tokenResponse = null;
        // Token Exchange and User Info Retrieval
        try{
                Map<String, String> redirectDetails = getRedirectUriAndClientDetails(provider, platform);
                System.out.println("Redirect details obtained: " + redirectDetails.keySet());
                String redirectUri = redirectDetails.get("redirectUri");
                String clientId = redirectDetails.get("clientId");
                String clientSecret = redirectDetails.get("clientSecret");
                System.out.println("Calling exchangeCodeForToken...");
                tokenResponse = exchangeCodeForToken(provider, code, redirectUri, clientId, clientSecret, codeVerifier);
                System.out.println("Token exchange successful, got token keys: " + tokenResponse.keySet());
            } catch (Exception e) {
                System.out.println("Token exchange failed: " + e.getMessage());
                e.printStackTrace();
                return handleValidationError("Token exchange failed: " + e.getMessage(), session);
            }
            String accessToken = tokenResponse.get("access_token");
            System.out.println("Access token obtained: " + (accessToken != null ? "yes" : "no"));
            Map<String, Object> userInfo;
            try {
                System.out.println("Fetching user info...");
                userInfo = fetchUserInfo(provider, accessToken);
                System.out.println("User info fetched successfully: " + userInfo.keySet());
            } catch (Exception e) {
                System.out.println("Failed to fetch user info: " + e.getMessage());
                e.printStackTrace();
                return handleValidationError("Failed to fetch user info: " + e.getMessage(), session);
            }

            System.out.println("Creating/updating user...");
            User user = userService.createOrUpdateOAuthUser(provider, userInfo);
            System.out.println("User created/updated: " + user.getUsername());
            String jwtToken = generateJwtToken(user);
            System.out.println("JWT token generated");
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("token", jwtToken);
            
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", user.getUserId());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail() != null ? user.getEmail() : "");
            userMap.put("provider", user.getProvider());
            userData.put("user", userMap);

            if ("android".equals(platform)) {
                // For Android (all providers): Deep link redirect with JWT
                String deepLinkUrl = buildDeepLinkUrl(jwtToken, userData);
                return new RedirectView(deepLinkUrl);
            } else if ("web".equals(platform)) {
                // For Web: Frontend redirect with fragment data
                String redirectUrl = buildWebRedirectUrl(jwtToken, userData);
                return new RedirectView(redirectUrl);
            } else {
                // Fallback to JSON for unknown platforms
                return ResponseEntity.ok(Map.of("token", jwtToken, "user", userData));
            }
    }

    @PostMapping("/google/android/token")
    public ResponseEntity<Map<String, Object>> exchangeGoogleAndroidToken(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extract access token and user info from request
            String accessToken = (String) requestBody.get("access_token");
            @SuppressWarnings("unchecked")
            Map<String, Object> userInfo = (Map<String, Object>) requestBody.get("user_info");

            if (accessToken == null || userInfo == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing access_token or user_info"));
            }

            // Create or update user in database
            User user = userService.createOrUpdateOAuthUser("google", userInfo);
            String jwtToken = generateJwtToken(user);

            // Return JWT token and user data
            Map<String, Object> response = Map.of(
                "token", jwtToken,
                "user", Map.of(
                    "userId", user.getUserId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "provider", user.getProvider()
                )
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Token exchange failed: " + e.getMessage()));
        }
    }

    private String buildDeepLinkUrl(String jwtToken, Map<String, Object> userData) {
        // Use your Android app's deep link scheme
        String baseUrl = "umastagram://auth/callback";
        String queryParams = buildFragmentData(jwtToken, userData);
        return baseUrl + "?" + queryParams;
    }

    private String buildDeepLinkUrlForCode(String code, String state) {
        // Use your Android app's deep link scheme for Google Android (PKCE flow)
        String baseUrl = "umastagram://auth/callback";
        String queryParams = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
            "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8) +
            "&provider=google&platform=android";
        return baseUrl + "?" + queryParams;
    }

    private String buildWebRedirectUrl(String jwtToken, Map<String, Object> userData) {
        String fragment = buildFragmentData(jwtToken, userData);
        return webSuccessRedirectUrl + "#" + fragment;
    }

    private String buildFragmentData(String jwtToken, Map<String, Object> userData) {
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) userData.get("user");
        
        return "token=" + URLEncoder.encode(jwtToken, StandardCharsets.UTF_8) +
            "&userId=" + URLEncoder.encode(user.get("userId").toString(), StandardCharsets.UTF_8) +
            "&username=" + URLEncoder.encode((String)user.get("username"), StandardCharsets.UTF_8) +
            "&email=" + URLEncoder.encode((String)user.get("email"), StandardCharsets.UTF_8) +
            "&provider=" + URLEncoder.encode((String)user.get("provider"), StandardCharsets.UTF_8);
    }

    public String generateJwtToken(User user){
        // Step 1: Set expiration time (e.g., 24 hours)
        long expirationTime = 86400000; // 24 hours in milliseconds
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
        
        // Step 2: Create signing key from secret
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        // Step 3: Build and sign the JWT
        return Jwts.builder()
                .setSubject(user.getUserId().toString())  // User ID as subject
                .claim("email", user.getEmail())     // Additional claims
                .claim("username", user.getUsername())
                .claim("provider", user.getProvider())
                .setIssuedAt(new Date())                   // Token issued time
                .setExpiration(expirationDate)             // Token expiration time
                .signWith(key)                             // Sign with the key
                .compact();                                 // Build the final token
    }

    

    private Map<String,String> getRedirectUriAndClientDetails(String provider, String platform) {
        Map<String, String> details = new HashMap<>();
        if (platform.equals("android")) {
            if (provider.equals("github")) {
                details.put("redirectUri", githubAndroidRedirectUri);
                details.put("clientId", githubAndroidClientId);
                details.put("clientSecret", githubAndroidClientSecret);
            } else if (provider.equals("google")) {
                details.put("redirectUri", googleAndroidRedirectUri);
                details.put("clientId", googleAndroidClientId);
                // Google Android uses PKCE - no client secret needed
                details.put("clientSecret", "");
            } else {
                throw new IllegalArgumentException("Unsupported provider: " + provider);
            }
        } else if (platform.equals("web")) {
            if (provider.equals("github")) {
                details.put("redirectUri", githubWebRedirectUri);
                details.put("clientId", githubWebClientId);
                details.put("clientSecret", githubClientSecret);
            } else if (provider.equals("google")) {
                details.put("redirectUri", googleWebRedirectUri);
                details.put("clientId", googleWebClientId);
                details.put("clientSecret", googleClientSecret);
            } else {
                throw new IllegalArgumentException("Unsupported provider: " + provider);
            }
        } else {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
        return details;
    }
}
