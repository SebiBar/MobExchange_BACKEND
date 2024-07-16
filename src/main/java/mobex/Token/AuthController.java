package mobex.Token;

import mobex.User.User;
import mobex.User.UserDTO;
import mobex.User.UserRepository;
import mobex.User.UserService;
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
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthController(UserService userService, AuthService authService, TokenRepository tokenRepository) {
        this.userService = userService;
        this.authService = authService;
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
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken){
        try{
            accessToken = accessToken.substring(7); // Remove "Bearer " prefix
            Optional<Token> tokenOptional = tokenRepository.findTokenByAccessToken(accessToken);
            tokenOptional.ifPresent(tokenRepository::delete);
            return new ResponseEntity<>("Token deleted", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }
}
