package org.webtree.trust.controller.alfa;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.webtree.trust.provider.Provider;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest/alfa")
public class TrustController {

    public TrustController() {
    }

    @GetMapping("/trust")
    public ResponseEntity<?> getTrust(@RequestParam("provider") Provider provider, @RequestParam("id") String id) {
        System.out.println(provider + " " + id);
        return ResponseEntity.ok(0);

    }
     /*temporal handler here*/
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> noProviderException(HttpServletRequest request) {
        return ResponseEntity.badRequest().body("No such provider: " + request.getParameter("provider"));
    }
}
