package mobex.Token;

import jakarta.transaction.Transactional;
import mobex.User.*;
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
    private final EmailValidator emailValidator;


    @Transactional
    public User registerUser(User user){
        String userEmail = user.getEmail();
        if(emailValidator.isValidEmail(userEmail)) {
            throw new RuntimeException(("Email is not valid " + userEmail));
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        if (userRepository.findByEmail(userEmail).isPresent()) {
            throw new RuntimeException(("Email already used " + userEmail));
        }

        return userRepository.save(user); // Returnează utilizatorul salvat pentru a obține ID-ul său generat
    }

    @Autowired
    public AuthService(UserService userService, TokenRepository tokenRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EmailValidator emailValidator) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailValidator = emailValidator;
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

    public User getUserByAccessToken(String accessToken) {
        Token token = getTokenByAccessToken(accessToken);
        return token.getUser();
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


}
