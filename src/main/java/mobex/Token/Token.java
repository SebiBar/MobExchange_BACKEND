package mobex.Token;

import jakarta.persistence.*;
import mobex.User.User;

@Table(name = "tokens")
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    public Integer token_id;
    @Column(nullable = false)
    public String accessToken;
    @Column(nullable = false)
    public String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
}
