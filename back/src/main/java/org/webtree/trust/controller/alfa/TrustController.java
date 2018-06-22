package org.webtree.trust.controller.alfa;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.webtree.trust.controller.UserController;
import org.webtree.trust.provider.Provider;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest/alfa")
public class TrustController {

    public TrustController() {
    }

    @GetMapping("/trust")
    public ResponseEntity<?> getTrust(@RequestParam("provider") Provider provider, @RequestParam("userId") String id) {
        return ResponseEntity.ok(0.5F);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public @ResponseBody String noProviderExistsException(HttpServletRequest request) {
        String msg = "No such provider: ";
        return (msg + request.getParameter("provider"));
    }
}
