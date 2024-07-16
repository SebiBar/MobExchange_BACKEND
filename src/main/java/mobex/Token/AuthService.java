package mobex.Token;

import jakarta.transaction.Transactional;
import mobex.User.User;
import mobex.User.UserDetailsDTO;
import mobex.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public TokenDTO createTokenReturnDTO(User user) {
        Token token = this.createToken(user);
        return new TokenDTO(
                token.getAccessToken(),
                token.getRefreshToken()
        );
    }

    public TokenDTO replaceOldToken(String refreshToken) {
        Optional<Token> tokenOptional = tokenRepository.findTokenByRefreshToken(refreshToken);
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
            else
                throw new RuntimeException("Access token expired");
        }
       throw new RuntimeException("User not found");
    }

/*    public User getUserByAccessToken(String accessToken) {
        Optional<Token> tokenOptional = tokenRepository.findTokenByAccessToken(accessToken);
        if (tokenOptional.isPresent()) {
            Token token = tokenOptional.get();
            return token.getUser();
        }
        throw new RuntimeException("Invalid access token");
    }*/

    @Transactional
    public void deleteToken(String accessToken) {
        Optional<Token> tokenOptional = tokenRepository.findTokenByAccessToken(accessToken);

        if (tokenOptional.isPresent()) {
            tokenRepository.delete(tokenOptional.get());
        }
        else{
            throw new RuntimeException("Token not found");
        }

    }

    private String generateTokenValue() {
        return UUID.randomUUID().toString();
    }


}
