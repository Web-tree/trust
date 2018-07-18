package org.webtree.trust.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

/**
 * Created by Udjin on 21.03.2018.
 */
@ControllerAdvice(annotations = RestController.class)
public class SecurityControllerAdvice {

    private MessageSource messageSource;
    private static final String LOGIN_ERROR = "login.badCredentials";

    @Autowired
    public SecurityControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> badUserNameHandler() {
        return createError(LOGIN_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badPasswordHandler() {
        return createError(LOGIN_ERROR);
    }

    private ResponseEntity<String> createError(String errorCode) {
        String errorMessage = messageSource.getMessage(errorCode, new Object[]{}, Locale.getDefault());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

}
