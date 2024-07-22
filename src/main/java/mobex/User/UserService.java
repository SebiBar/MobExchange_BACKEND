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
        String userFirstName = user.getFirstname();
        if(!nameValidator.isValidName(userFirstName) ){
            throw new RuntimeException("First name is not valid " + userFirstName);
        }

        String userLastName = user.getLastname();
        if(!nameValidator.isValidName(userLastName) ){
            throw new RuntimeException("Last name is not valid " + userLastName);
        }

        String userEmail = user.getEmail();
        if(!emailValidator.isValidEmail(userEmail)) {
            throw new RuntimeException(("Email is not valid " + userEmail));
        }
        if (userRepository.findByEmail(userEmail).isPresent()) {
            throw new RuntimeException(("Email already used " + userEmail));
        }

        String userPassword = user.getPassword();
        if(!passwordValidator.isValidPassword(userPassword)){
            throw new RuntimeException(("Password must contain only letters and/or digits and must be between 6 and 20 characters"));
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

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
                throw new RuntimeException("Incorrect Password");
            }
        }
        else {
            throw new RuntimeException("User not found");
        }
    }

    public void changePassword(User user, PasswordDTO passwordDTO){
        String oldPassword = passwordDTO.getOldPassword();
        if(!passwordValidator.isValidPassword(oldPassword)){
            throw new RuntimeException(("Old password must contain only letters and/or digits and must be between 6 and 20 characters"));
        }
        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new RuntimeException("Old password does not match with user's password");
        }

        String newPassword = passwordDTO.getNewPassword();
        if(!passwordValidator.isValidPassword(newPassword)){
            throw new RuntimeException(("New password must contain only letters and/or digits and must be between 6 and 20 characters"));
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
