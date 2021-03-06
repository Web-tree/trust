package org.webtree.trust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.trust.domain.AuthDetails;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.service.TrustUserService;
import org.webtree.trust.service.security.JwtTokenService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest")
public class SecurityController extends AbstractController {

    private final AuthenticationManager authenticationManager;

    @Value("${jwt.header}")
    private String tokenHeader;

    private JwtTokenService jwtTokenService;
    private TrustUserService trustUserService;

    @Autowired
    public SecurityController(AuthenticationManager authenticationManager,
                              JwtTokenService jwtTokenService,
                              TrustUserService trustUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.trustUserService = trustUserService;
    }

    @PostMapping("${jwt.route.authentication.path}")
    public ResponseEntity<?> login(@RequestBody AuthDetails authDetails) {
        int passwordLength = authDetails.getPassword().length();

        if (passwordLength != 128) {
            return ResponseEntity.badRequest().body("The password must be a representation of sha512");
        }

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        authDetails.getUsername(), authDetails.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TrustUser trustUser = trustUserService.loadUserByUsername(authDetails.getUsername());
        return ResponseEntity.ok(jwtTokenService.generateToken(trustUser));
    }

    @PostMapping
    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenService.getUsernameFromToken(token);
        TrustUser trustUser = trustUserService.loadUserByUsername(username);

        if (jwtTokenService.canTokenBeRefreshed(token, trustUser.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenService.refreshToken(token);
            return ResponseEntity.ok(refreshedToken);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
