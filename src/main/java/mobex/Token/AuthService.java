package mobex.Token;

import mobex.User.User;
import mobex.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
                UUID.randomUUID().toString(),       //accessToken
                LocalDateTime.now().plusHours(1),   //accessTokenExpires
                UUID.randomUUID().toString(),       //refreshToken
                LocalDateTime.now().plusHours(12),  //refreshTokenExpires
                user);                              //user

        tokenRepository.save(token);

        return token;
    }

    private String generateTokenValue() {
        return UUID.randomUUID().toString();
    }
}
