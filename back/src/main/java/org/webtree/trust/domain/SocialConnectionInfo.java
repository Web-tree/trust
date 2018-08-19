package org.webtree.trust.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.webtree.trust.provider.Provider;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialConnectionInfo {
    private String provider;
    private String token;
}
