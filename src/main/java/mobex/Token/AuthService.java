package mobex.Token;

import jakarta.transaction.Transactional;
import mobex.User.User;
import mobex.User.UserDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

// TODO: CHANGE METHOD TYPE TO TOKEN INSTEAD OF STRING

@Service
public class AuthService {

    private final TokenRepository tokenRepository;

    @Autowired
    public AuthService(TokenRepository tokenRepository) {
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

    @Transactional
    public TokenDTO replaceValidOldToken(String refreshToken) {
        Token token = getValidTokenByRefreshToken(refreshToken);
        User user = token.getUser();
        tokenRepository.delete(token);  //deletes old token
        return this.createTokenReturnDTO(user); // returns new token in DTO form
    }

    public UserDetailsDTO getValidUserDetails(String accessToken) {
        Token token = getValidTokenByAccessToken(accessToken);
        if (token.isValid()) {    //if after today -> not expired
            User user = token.getUser();
            return new UserDetailsDTO(
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail()
            );
        } else
            throw new RuntimeException("Access token expired");
    }

    @Transactional
    public void deleteValidToken(String accessToken) {
        Token token = getValidTokenByAccessToken(accessToken);
        tokenRepository.delete(token);
    }

    public Token getTokenByAccessToken(String accessToken) {
        accessToken = accessToken.replace("Bearer ", "");
        Optional<Token> tokenOptional = tokenRepository.findTokenByAccessToken(accessToken);
        if (tokenOptional.isPresent()) {
            return tokenOptional.get();
        }
        throw new RuntimeException("Token not found");
    }

    public Token getTokenByRefreshToken(String refreshToken) {
        refreshToken = refreshToken.replace("Bearer ", "");
        Optional<Token> tokenOptional = tokenRepository.findTokenByRefreshToken(refreshToken);
        if (tokenOptional.isPresent()) {
            return tokenOptional.get();
        }
        throw new RuntimeException("Token not found");
    }

    public Token getValidTokenByAccessToken(String accessToken) {
        Token token = getTokenByAccessToken(accessToken);
        if (token.isValid()) {
            return token;
        }
        throw new RuntimeException("Token has expired");
    }

    public Token getValidTokenByRefreshToken(String refreshToken) {
        Token token = getTokenByRefreshToken(refreshToken);
        if (token.isValidRefreshToken()) {
            return token;
        }
        throw new RuntimeException("Token has expired");
    }

    private String generateTokenValue() {
        return UUID.randomUUID().toString();
    }


}
