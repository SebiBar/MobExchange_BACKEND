package mobex.Token;

import jakarta.persistence.*;
import lombok.*;
import mobex.User.User;

import java.time.LocalDateTime;



@Table(name = "tokens")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Token {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long token_id;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private LocalDateTime accessTokenExpires;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime refreshTokenExpires;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Token(String s, LocalDateTime localDateTime, String s1, LocalDateTime localDateTime1, User user) {
        this.accessToken = s;
        this.accessTokenExpires = localDateTime;
        this.refreshToken = s1;
        this.refreshTokenExpires = localDateTime1;
        this.user = user;
    }

    public Boolean isValid() {  //if after today -> not expired
        return getAccessTokenExpires().isAfter(LocalDateTime.now());
    }

    public Boolean isValidRefreshToken(){
        return getRefreshTokenExpires().isAfter(LocalDateTime.now());
    }
}
