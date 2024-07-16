package mobex.Token;

import jakarta.transaction.Transactional;
import mobex.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public Token createToken(User user){
        Token token = new Token(user);
        return tokenRepository.save(token);
    }
}
