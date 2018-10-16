package org.webtree.trust.controller.alfa;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.webtree.trust.provider.Provider;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
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
