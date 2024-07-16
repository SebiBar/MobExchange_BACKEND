package mobex.User;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final EmailValidator emailValidator;

    @Autowired
    public UserService(EmailValidator emailValidator){
        this.emailValidator = emailValidator;
    }

    @Transactional
    public User registerUser (User userDTO) {
        try {
            String userEmail = userDTO.getEmail();
            if(!emailValidator.isValidEmail(userEmail))
                throw new EmailNotValidException("Email is not valid: " + userEmail);
            return userRepository.save(userDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }

    public UserDTO loginUser(UserDTO userDTO){
        Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(user.getPassword().equals(userDTO.getPassword())){
                return userDTO;
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
