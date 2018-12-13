package org.webtree.trust.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;

/**
 * Created by Udjin Skobelev on 02.11.2018.
 */

@Table
public class Application {

    @PrimaryKey
    private String id;

    @Indexed
    @JsonIgnore
    private String trustUserId;

    private String name;

    @JsonIgnore
    private String secret;

    public void setTrustUserId(String trustUserId) {
        this.trustUserId = trustUserId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getId() {
        return id;
    }

    public String getTrustUserId() {
        return trustUserId;
    }

    public String getName() {
        return name;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(trustUserId, that.trustUserId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(secret, that.secret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trustUserId, name, secret);
    }

    public static final class Builder {
        private String id;
        private String trustUserId;
        private String name;
        private String clientSecret;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder trustUserId(String trustUserId) {
            this.trustUserId = trustUserId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Application build() {
            Application application = new Application();
            application.setId(id);
            application.setTrustUserId(trustUserId);
            application.setName(name);
            application.setSecret(clientSecret);
            return application;
        }
    }
}
