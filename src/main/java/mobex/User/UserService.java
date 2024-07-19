package mobex.User;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final NameValidator nameValidator;
    private final PasswordValidator passwordValidator;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       EmailValidator emailValidator, NameValidator nameValidator,
                       PasswordValidator passwordValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailValidator = emailValidator;
        this.nameValidator = nameValidator;
        this.passwordValidator = passwordValidator;
    }

    @Transactional
    public User registerUser(User user){
        String userEmail = user.getEmail();
        String userFirstName = user.getFirstname();
        String userLastName = user.getLastname();
        String userPassword = user.getPassword();
        if(!nameValidator.isValidName(userFirstName) ){
            throw new RuntimeException("First name is not valid " + userFirstName);
        }
        if(!nameValidator.isValidName(userLastName) ){
            throw new RuntimeException("Last name is not valid " + userLastName);
        }
        if(!emailValidator.isValidEmail(userEmail)) {
            throw new RuntimeException(("Email is not valid " + userEmail));
        }
        if(userPassword == null || userPassword.length() < 6 || userPassword.length() > 20) {
            throw new RuntimeException("Password must be between 6 and 20 characters");
        }
        if(!passwordValidator.isValidPassword(userPassword)){
            throw new RuntimeException(("Password is not valid"));
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        if (userRepository.findByEmail(userEmail).isPresent()) {
            throw new RuntimeException(("Email already used " + userEmail));
        }

        userRepository.save(user);
        return user;
    }

    @Transactional
    public User loginUser(UserDTO userDTO){
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(passwordEncoder.matches(userDTO.getPassword(), user.getPassword())){
                return user;
            }
            else {
                throw new RuntimeException("Invalid Password");
            }
        }
        else {
            throw new RuntimeException("User not found");
        }
    }
}
