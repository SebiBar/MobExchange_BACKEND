package mobex.Token;

import mobex.User.User;
import mobex.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private String generateTokenValue() {
        return UUID.randomUUID().toString();
    }
}
