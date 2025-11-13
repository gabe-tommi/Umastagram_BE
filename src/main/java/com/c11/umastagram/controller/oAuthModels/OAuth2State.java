package com.c11.umastagram.controller.oAuthModels;

/**
 * OAuth2State class for storing OAuth2 authorization request state information.
 * Used for CSRF protection and request validation in OAuth2 flows.
 *
 * @author Armando Vega
 * @version 1.0
 * @since 2025-11-09
 */
public class OAuth2State {
    private final String provider;
    private final String platform;
    private final long createdAt;

    /**
     * Creates a new OAuth2State instance.
     *
     * @param provider the OAuth2 provider (e.g., "github", "google")
     * @param platform the platform (e.g., "android", "web")
     * @param createdAt the timestamp when the state was created (milliseconds since epoch)
     */
    public OAuth2State(String provider, String platform, long createdAt) {
        this.provider = provider;
        this.platform = platform;
        this.createdAt = createdAt;
    }

    public String getProvider() {
        return provider;
    }


    public String getPlatform() {
        return platform;
    }


    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * Checks if the state has expired.
     *
     * @param maxAgeMillis the maximum age in milliseconds (e.g., 15 minutes = 15 * 60 * 1000)
     * @return true if the state has expired, false otherwise
     */
    public boolean isExpired(long maxAgeMillis) {
        return (System.currentTimeMillis() - createdAt) > maxAgeMillis;
    }

    @Override
    public String toString() {
        return "OAuth2State{" +
                "provider='" + provider + '\'' +
                ", platform='" + platform + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}