package mobex.Token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findTokenByAccessToken(String token);
    Optional<Token> findTokenByRefreshToken(String token);
}
