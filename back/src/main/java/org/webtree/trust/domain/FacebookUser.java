package org.webtree.trust.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Builder
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
}


