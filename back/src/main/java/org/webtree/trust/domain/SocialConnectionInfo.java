package org.webtree.trust.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialConnectionInfo {
    @NotNull
    private String provider;
    @NotNull
    private String token;
}
