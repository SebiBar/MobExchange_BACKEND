package mobex.User;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{6,20}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public boolean isValidPassword(String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
