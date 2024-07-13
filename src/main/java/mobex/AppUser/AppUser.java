package mobex.AppUser;

import jakarta.persistence.*;

@Table(name = "app_user")
@Entity
public class AppUser extends EmailValidator{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    public Long getUserId() {
        return userId;
    }

    @Column
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    @Column
    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    @Column
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    @Column
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    @Column
    public void setEmail(String email) {
        try
        {
            isValidEmail(email);
            this.email = email;
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public String getPassword() {
        return password;
    }

    @Column
    public void setPassword(String password) {
        this.password = password;
    }

    public AppUser(Long userId, String username, String first_name, String last_name, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public AppUser() {
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
