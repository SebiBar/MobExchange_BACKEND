package mobex.Token;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobex.User.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reset_password_tokens")
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long resetPasswordToken_id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ResetPasswordToken(User user){
        this.token = UUID.randomUUID().toString();
        this.expirationDate = LocalDateTime.now().plusMinutes(10);
        this.user = user;
    }

    public Boolean isValid(){
        return getExpirationDate().isAfter(LocalDateTime.now());
    }
}
