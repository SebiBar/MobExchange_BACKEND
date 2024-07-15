package mobex.Token;

import jakarta.persistence.*;
import mobex.AppUser.AppUser;

@Table(name = "token")
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long tokenId;

    @Column(nullable = false)
    private String accesToken;

    @Column(nullable = false)
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "appUser")
    public AppUser user;
}
