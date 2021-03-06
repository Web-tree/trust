package org.webtree.trust.boot.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.trust.exception.ProfileAlreadyLinkedWithAnotherAccountException;
import org.webtree.trust.exception.ProfileAlreadyLinkedWithCurrentAccountException;
import org.webtree.trust.exception.UnexpectedSocialRepositoryResponseException;
import org.webtree.trust.exception.UserAlreadyHasIdException;
import org.webtree.trust.service.exception.ProviderNotSupportedException;

import java.util.Locale;

/**
 * Created by Udjin on 21.03.2018.
 */
@ControllerAdvice(annotations = RestController.class)
public class SecurityControllerAdvice {

    private MessageSource messageSource;
    private static final String LOGIN_ERROR = "login.badCredentials";
    private static final String SOCIAL_LINKED_CURRENT = "social.linked.current";
    private static final String SOCIAL_LINKED_ANOTHER = "social.linked.another";
    private static final String PROVIDER_NOT_SUPPORTED = "social.provider.notSupported";
    private static final String CANNOT_SAVE_DATA = "social.data.save.exception";
    private static final String ALREADY_HAS_ID = "user.create.exception";
    private static final String INVALID_ACCESS_TOKEN = "social.invalidToken";

    @Autowired
    public SecurityControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> badUserNameHandler() {
        return createUnauthorizedError(LOGIN_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badPasswordHandler() {
        return createUnauthorizedError(LOGIN_ERROR);
    }

    @ExceptionHandler(ProfileAlreadyLinkedWithAnotherAccountException.class)
    public ResponseEntity<String> linkedWithAnotherHandler() {
        return createBadRequestError(SOCIAL_LINKED_ANOTHER);
    }

    @ExceptionHandler(ProfileAlreadyLinkedWithCurrentAccountException.class)
    public ResponseEntity<String> linkedWithCurrentHandler() {
        return createBadRequestError(SOCIAL_LINKED_CURRENT);
    }

    @ExceptionHandler(ProviderNotSupportedException.class)
    public ResponseEntity<String> providerNotSupportedHandler() {
        return createBadRequestError(PROVIDER_NOT_SUPPORTED);
    }

    @ExceptionHandler(UserAlreadyHasIdException.class)
    public ResponseEntity<String> incorrectDataPassedHandler() {
        return createBadRequestError(ALREADY_HAS_ID);
    }

    @ExceptionHandler(UnexpectedSocialRepositoryResponseException.class)
    public ResponseEntity<String> cantSaveInDbHandler() {
        return createBadRequestError(CANNOT_SAVE_DATA);
    }

    @ExceptionHandler(InvalidAuthorizationException.class)
    public ResponseEntity<String> invalidAccessTokenHandler() {
        return createBadRequestError(INVALID_ACCESS_TOKEN);
    }

    private ResponseEntity<String> createBadRequestError(String errorCode) {
        String errorMessage = messageSource.getMessage(errorCode, new Object[]{}, Locale.getDefault());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> createUnauthorizedError(String errorCode) {
        String errorMessage = messageSource.getMessage(errorCode, new Object[]{}, Locale.getDefault());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }
}
