package pt.iti.umdrive.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        log.error("Bad credentials exception: {} - username: [{}] - password: [{}] - returning code: [{}]",
                ex.getMessage(),
                request.getHeader("username"),
                request.getHeader("password"),
                HttpStatus.UNAUTHORIZED,
                ex);

        return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
