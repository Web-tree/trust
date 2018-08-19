package org.webtree.trust.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Data
@Builder
@Table("facebook_user")
public class FacebookUser {
    @PrimaryKey
    private String id; //Facebook userId
    private String trustUserId; // our userId
    private String email;
    private String firstName;
    private String gender;
    private String lastName;
    private String link;
   /* private Locale locale;*/
    private String name;
}


