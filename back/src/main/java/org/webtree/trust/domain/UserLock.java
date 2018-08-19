package org.webtree.trust.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * Created by Udjin Skobelev on 16.08.2018.
 */

@Data
@NoArgsConstructor
@Table("username_lock")
public class UserLock {
    @PrimaryKey
    private String username;

    public UserLock(String username) {
        this.username = username;
    }
}
