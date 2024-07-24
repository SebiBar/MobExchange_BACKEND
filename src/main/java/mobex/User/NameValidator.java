package mobex.User;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NameValidator {
    private static final String NAME_PATTERN = "^[a-zA-Z]{2,20}$";

    private static final Pattern pattern = Pattern.compile(NAME_PATTERN);

    public boolean isValidName(String name) {
        if(name == null) {
            return true;
        }
        Matcher matcher = pattern.matcher(name);
        return !matcher.matches();
    }
}
