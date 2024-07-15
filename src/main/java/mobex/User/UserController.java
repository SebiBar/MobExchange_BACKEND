package mobex.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String hello(){
        return "Hello";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User userData){
        try{
            User newUser = userService.registerUser(userData);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>("Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO ){
        try{
            UserDTO newUserDTO = userService.loginUser(userDTO);
            return new ResponseEntity<>(newUserDTO, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }
    }


}

