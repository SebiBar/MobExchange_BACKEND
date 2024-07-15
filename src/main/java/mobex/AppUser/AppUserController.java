package mobex.AppUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppUserController {

    @Autowired
    private AppUserServiceImpl appUserServiceImpl;

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);
    @GetMapping("/")
    public String hello(){
        return "Hello World";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppUser appUser){

        try {
            AppUser newUser = appUserServiceImpl.register(appUser);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (EmailNotValidException e) {
            logger.error("Error registering user", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        logger.error("Unhandled exception", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
