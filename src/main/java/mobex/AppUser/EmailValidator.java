package mobex.AppUser;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Component
public class EmailValidator {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public boolean isValidEmail(String email) {
        if(email == null)
            return false;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
