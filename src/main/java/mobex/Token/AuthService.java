package mobex.Token;

import jakarta.transaction.Transactional;
import mobex.User.User;
import mobex.User.UserDetailsDTO;
import mobex.User.UserRepository;
import mobex.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public AuthService(UserService userService, UserRepository userRepository, TokenRepository tokenRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
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
    public TokenDTO replaceOldToken(String refreshToken) {
        Token token = getTokenByRefreshToken(refreshToken);
        User user = token.getUser();
        tokenRepository.delete(token);  //deletes old token
        return this.createTokenReturnDTO(user); // returns new token in DTO form
    }

    public UserDetailsDTO getUserDetails(String accessToken) {
        Token token = getTokenByAccessToken(accessToken);
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
    public void deleteToken(String accessToken) {
        Token token = getTokenByAccessToken(accessToken);
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

    private String generateTokenValue() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid old password");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

}
