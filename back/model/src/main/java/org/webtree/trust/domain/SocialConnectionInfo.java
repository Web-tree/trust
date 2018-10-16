package org.webtree.trust.domain;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;

public class SocialConnectionInfo {
    @NotEmpty
    private String token;
    @NotEmpty
    private String provider;

    public SocialConnectionInfo(String provider, String token) {
        this.provider = provider;
        this.token = token;
    }

    public SocialConnectionInfo() {
    }

    public static SocialConnectionInfoBuilder builder() {
        return new SocialConnectionInfoBuilder();
    }

    public String getProvider() {
        return this.provider;
    }

    public String getToken() {
        return this.token;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SocialConnectionInfo)) return false;
        SocialConnectionInfo that = (SocialConnectionInfo) o;
        return Objects.equals(provider, that.provider) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, token);
    }

    @Override
    public String toString() {
        return "SocialConnectionInfo{" +
                "provider='" + provider + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public static class SocialConnectionInfoBuilder {
        private String provider;
        private String token;

        SocialConnectionInfoBuilder() {
        }

        public SocialConnectionInfo.SocialConnectionInfoBuilder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public SocialConnectionInfo.SocialConnectionInfoBuilder token(String token) {
            this.token = token;
            return this;
        }

        public SocialConnectionInfo build() {
            return new SocialConnectionInfo(provider, token);
        }
    }
}
