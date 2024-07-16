package mobex.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "users")
@Entity
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id", nullable = false)
    private Long user_id;

    @Column(name="firstname", nullable = false)
    private String firstname;

    @Column(name="lastname", nullable = false)
    private String lastname;

    @Column(name="email", unique = true, length = 100, nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

}

