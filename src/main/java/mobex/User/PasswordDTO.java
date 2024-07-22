package mobex.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordDTO {
    private String oldPassword;
    private String newPassword;
}
