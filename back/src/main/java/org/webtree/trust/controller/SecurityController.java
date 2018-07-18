package org.webtree.trust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.webtree.trust.domain.AuthDetals;
import org.webtree.trust.domain.User;
import org.webtree.trust.security.JwtTokenUtil;
import org.webtree.trust.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest")
public class SecurityController extends AbstractController {

    private final AuthenticationManager authenticationManager;

    @Value("${jwt.header}")
    private String tokenHeader;

    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;

    @Autowired
    public SecurityController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("${jwt.route.authentication.path}")
    public ResponseEntity<?> login(@RequestBody AuthDetals authDetals/*, Device device*/) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        authDetals.getUsername(), authDetals.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.loadUserByUsername(authDetals.getUsername());
        return ResponseEntity.ok(jwtTokenUtil.generateToken(user/*, device)*/));
    }

    @PostMapping
    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(refreshedToken);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
