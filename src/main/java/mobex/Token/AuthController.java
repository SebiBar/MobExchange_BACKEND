package mobex.Token;

import mobex.User.User;
import mobex.User.UserDTO;
import mobex.User.UserRepository;
import mobex.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthController(UserService userService, AuthService authService,
                          UserRepository userRepository, TokenRepository tokenRepository) {
        this.userService = userService;
        this.authService = authService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userData){
        try{
            User user = userService.registerUser(userData);
            TokenDTO tokenDTO = authService.createTokenReturnDTO(user);

            return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>("Email is already in use",HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        try{
            Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail()); //find user
            if(userOptional.isPresent()) {  //if exists

                User user = userOptional.get(); // gets user info

                if(user.getPassword().equals(userDTO.getPassword())){ //verifies pw

                    TokenDTO tokenDTO = authService.createTokenReturnDTO(user);    //makes tokenDTO
                    return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
                }}
            return new ResponseEntity<>("Invalid credentials",HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            return new ResponseEntity<>("Invalid credentials",HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken){
        try{
             accessToken = accessToken.substring(7);
            Optional<Token> tokenOptional = tokenRepository.findTokenByAccessToken(accessToken);
            tokenOptional.ifPresent(tokenRepository::delete);
            return new ResponseEntity<>("Token deleted",HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("scsdcsd",HttpStatus.UNAUTHORIZED);
        }
    }
}

