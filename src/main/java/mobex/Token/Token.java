package mobex.Token;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobex.User.User;

import java.util.Calendar;
import java.util.Date;

@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long token_id;
    @Column(nullable = false)
    private String accessToken;
    @Column(nullable = false)
    private String refreshToken;
    @Column(nullable = false)
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token(User user){
        setUser(user);
        setAccessToken(generateAccessToken());
        setRefreshToken(generateRefreshToken());
        setExpirationDate(calculateExpirationDate(60));
    }

    private String generateAccessToken() {
        // Implement access token generation logic
        return "access";
    }

    private String generateRefreshToken() {
        // Implement refresh token generation logic
        return "refresh";
    }
    private Date calculateExpirationDate(int expirationTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationTimeInMinutes);
        return calendar.getTime();
    }
}
