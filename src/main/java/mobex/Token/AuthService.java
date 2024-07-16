package mobex.Token;

import mobex.User.User;
import mobex.User.UserDetailsDTO;
import mobex.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserService userService;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthService(UserService userService, TokenRepository tokenRepository) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
    }

    public Token createToken(User user) {

        Token token = new Token(
                generateTokenValue(),               //accessToken
                LocalDateTime.now().plusHours(1),   //accessTokenExpires
                generateTokenValue(),               //refreshToken
                LocalDateTime.now().plusHours(12),  //refreshTokenExpires
                user);                              //user

        tokenRepository.save(token);

        return token;
    }

    public void logoutUser(User user) {

    }

    public TokenDTO createTokenReturnDTO(User user) {
        Token token = this.createToken(user);
        return new TokenDTO(
                token.getAccessToken(),
                token.getRefreshToken()
        );
    }

    public TokenDTO replaceOldToken(String accessToken) {
        Optional<Token> tokenOptional = tokenRepository.findTokenByAccessToken(accessToken);
        if (tokenOptional.isPresent()) {
            Token token = tokenOptional.get();
            User user = token.getUser();
            tokenRepository.delete(token);
            return this.createTokenReturnDTO(user);
        }
        return null;
    }

    public UserDetailsDTO getUserDetails(String accessToken) {
        Optional<Token> tokenOptional = tokenRepository.findTokenByAccessToken(accessToken);
        if (tokenOptional.isPresent()) {
            Token token = tokenOptional.get();
            if (token.isValid()){    //if after today -> not expired
                User user = token.getUser();
                return new UserDetailsDTO(
                        user.getFirstname(),
                        user.getLastname(),
                        user.getEmail()
                );
            }
            //return new ResponseEntity<>("Token expired",HttpStatus.UNAUTHORIZED);
        }
       return null;
    }

    private String generateTokenValue() {
        return UUID.randomUUID().toString();
    }
}
