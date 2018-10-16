package org.webtree.trust.domain;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;

@Table("facebook_user")
public class FacebookUser implements SocialUser {
    @PrimaryKey
    private String id;
    @Column("trust_user_id")
    private String trustUserId;
    private String email;
    private String firstName;
    private String gender;
    private String lastName;
    private String link;
    private String name;

    public FacebookUser(String id,
                        String trustUserId,
                        String email,
                        String firstName,
                        String gender,
                        String lastName,
                        String link,
                        String name) {
        this.id = id;
        this.trustUserId = trustUserId;
        this.email = email;
        this.firstName = firstName;
        this.gender = gender;
        this.lastName = lastName;
        this.link = link;
        this.name = name;
    }

    public static FacebookUserBuilder builder() {
        return new FacebookUserBuilder();
    }

    public String getId() {
        return this.id;
    }

    public String getTrustUserId() {
        return this.trustUserId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getGender() {
        return this.gender;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getLink() {
        return this.link;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTrustUserId(String trustUserId) {
        this.trustUserId = trustUserId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookUser)) return false;
        FacebookUser that = (FacebookUser) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(trustUserId, that.trustUserId) &&
                Objects.equals(email, that.email) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(link, that.link) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trustUserId, email, firstName, gender, lastName, link, name);
    }

    @Override
    public String toString() {
        return "FacebookUser{" +
                "id='" + id + '\'' +
                ", trustUserId='" + trustUserId + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", gender='" + gender + '\'' +
                ", lastName='" + lastName + '\'' +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static class FacebookUserBuilder {
        private String id;
        private String trustUserId;
        private String email;
        private String firstName;
        private String gender;
        private String lastName;
        private String link;
        private String name;

        FacebookUserBuilder() {
        }

        public FacebookUser.FacebookUserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public FacebookUser.FacebookUserBuilder trustUserId(String trustUserId) {
            this.trustUserId = trustUserId;
            return this;
        }

        public FacebookUser.FacebookUserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public FacebookUser.FacebookUserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public FacebookUser.FacebookUserBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public FacebookUser.FacebookUserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public FacebookUser.FacebookUserBuilder link(String link) {
            this.link = link;
            return this;
        }

        public FacebookUser.FacebookUserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public FacebookUser build() {
            return new FacebookUser(id, trustUserId, email, firstName, gender, lastName, link, name);
        }

        @Override
        public String toString() {
            return "FacebookUserBuilder{" +
                    "id='" + id + '\'' +
                    ", trustUserId='" + trustUserId + '\'' +
                    ", email='" + email + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", gender='" + gender + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", link='" + link + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}


