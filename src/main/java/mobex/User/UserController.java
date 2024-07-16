package mobex.User;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/")
    public String hello(){
        return "Hello";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User userData){

        try {
            User newUser = userService.registerUser(userData);
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
