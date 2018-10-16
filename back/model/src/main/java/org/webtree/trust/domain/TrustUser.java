package org.webtree.trust.domain;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Table("trust_user")
public class TrustUser implements UserDetails {

    private static final long serialVersionUID = -979784771401667331L;

    @PrimaryKey
    private String id;
    @Indexed
    private String username;
    @Column
    private String password;
    @Column
    private Date lastPasswordResetDate;

    public TrustUser(String id, String username, String password, Date lastPasswordResetDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public TrustUser() {
    }

    public static TrustUserBuilder builder() {
        return new TrustUserBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptySet();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getId() {
        return this.id;
    }

    public @Indexed String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(@Indexed String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "TrustUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrustUser)) return false;
        TrustUser trustUser = (TrustUser) o;
        return Objects.equals(id, trustUser.id) &&
                Objects.equals(username, trustUser.username) &&
                Objects.equals(password, trustUser.password);
    }

    public static class TrustUserBuilder {
        private String id;
        private @Indexed String username;
        private String password;
        private Date lastPasswordResetDate;

        TrustUserBuilder() {
        }

        public TrustUser.TrustUserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public TrustUser.TrustUserBuilder username(@Indexed String username) {
            this.username = username;
            return this;
        }

        public TrustUser.TrustUserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public TrustUser.TrustUserBuilder lastPasswordResetDate(Date lastPasswordResetDate) {
            this.lastPasswordResetDate = lastPasswordResetDate;
            return this;
        }

        public TrustUser build() {
            return new TrustUser(id, username, password, lastPasswordResetDate);
        }

    }
}
