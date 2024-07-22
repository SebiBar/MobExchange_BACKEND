package mobex.User;

import jakarta.mail.MessagingException;
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
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       EmailValidator emailValidator, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailValidator = emailValidator;
        this.emailService = emailService;
    }

    @Transactional
    public User registerUser(User user){
        String userEmail = user.getEmail();
        if(!emailValidator.isValidEmail(userEmail)) {
            throw new RuntimeException(("Email is not valid " + userEmail));
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

    @Transactional
    public void forgotPassword(String email) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            emailService.sendSetPasswordEmail(email);
        }
        else throw new RuntimeException("Email not found");
    }

    @Transactional
    public void setPassword(String password, String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            String hashedPassword = passwordEncoder.encode(password);
            user.setPassword(hashedPassword);
            userRepository.save(user);
        }
        else throw new RuntimeException("Invalid email address");
    }
}
