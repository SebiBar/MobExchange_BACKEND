package mobex.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordDTO {
    private String email;
    private String oldPassword;
    private String newPassword;
}
