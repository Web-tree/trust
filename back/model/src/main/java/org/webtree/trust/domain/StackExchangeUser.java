package org.webtree.trust.domain;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;

@Table("stackexchange_user")
public class StackExchangeUser implements SocialUser {
    @PrimaryKey
    private String id;
    @Column("trust_user_id")
    private String trustUserId;
    private Integer reputation;
    private Integer userId;
    private String link;
    private String profileImage;
    private String displayName;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrustUserId() {
        return this.trustUserId;
    }

    public void setTrustUserId(String id) {
        this.trustUserId = id;
    }

    public StackExchangeUser(String accountId,
                             String trustUserId,
                             Integer reputation,
                             Integer userId,
                             String link,
                             String profileImage,
                             String displayName) {
        this.id = accountId;
        this.trustUserId = trustUserId;
        this.reputation = reputation;
        this.userId = userId;
        this.link = link;
        this.profileImage = profileImage;
        this.displayName = displayName;
    }

    public StackExchangeUser() {
    }

    public Integer getReputation() {
        return this.reputation;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getLink() {
        return this.link;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            StackExchangeUser that = (StackExchangeUser) o;
            return Objects.equals(this.id, that.id) &&
                    Objects.equals(this.trustUserId, that.trustUserId) &&
                    Objects.equals(this.reputation, that.reputation) &&
                    Objects.equals(this.userId, that.userId) &&
                    Objects.equals(this.link, that.link) &&
                    Objects.equals(this.profileImage, that.profileImage) &&
                    Objects.equals(this.displayName, that.displayName);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(
                this.id,
                this.trustUserId,
                this.reputation,
                this.userId,
                this.link,
                this.profileImage,
                this.displayName);
    }

    public static final class Builder {
        private String accountId;
        private String trustUserId;
        private Integer reputation;
        private Integer userId;
        private String link;
        private String profileImage;
        private String displayName;

        private Builder() {
        }

        public static Builder asStackExchangeUser() {
            return new Builder();
        }

        public Builder withAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder withTrustUserId(String trustUserId) {
            this.trustUserId = trustUserId;
            return this;
        }

        public Builder withReputation(Integer reputation) {
            this.reputation = reputation;
            return this;
        }

        public Builder withUserId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder withLink(String link) {
            this.link = link;
            return this;
        }

        public Builder withProfileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public StackExchangeUser build() {
            return new StackExchangeUser(
                    this.accountId,
                    this.trustUserId,
                    this.reputation,
                    this.userId,
                    this.link,
                    this.profileImage,
                    this.displayName
            );
        }
    }
}