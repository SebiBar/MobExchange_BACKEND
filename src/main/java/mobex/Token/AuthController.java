package mobex.Token;

import mobex.User.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userData){
        try{
            User user = userService.registerUser(userData);
            TokenDTO tokenDTO = authService.createTokenReturnDTO(user);
            return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        try{
            User user = userService.loginUser(userDTO);
            TokenDTO tokenDTO = authService.createTokenReturnDTO(user);
            return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken){
        try{
            accessToken = accessToken.substring(7); // Remove "Bearer " prefix

            authService.deleteToken(accessToken);

            return new ResponseEntity<>("Token deleted", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String accessToken){
        try{
            accessToken = accessToken.substring(7);
            UserDetailsDTO userDetailsDTO = authService.getUserDetails(accessToken);
            return new ResponseEntity<>(userDetailsDTO, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/getNewRefreshToken")
    public ResponseEntity<?> getNewRefreshToken(@RequestHeader("Authorization") String refreshToken){
        try{
            refreshToken = refreshToken.substring(7);  // da cut la refreshToken la fel ca la access
            TokenDTO tokenDTO = authService.replaceOldToken(refreshToken);
            return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


}
