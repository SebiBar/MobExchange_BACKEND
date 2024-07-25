package mobex.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDetailsDTO {
    private String firstname;
    private String lastname;
    private String email;
}
