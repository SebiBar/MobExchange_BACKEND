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

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(User user){
        try{
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            userRepository.save(user);
            return user;
        }
        catch(Error e){
            throw new RuntimeException(e.getMessage());
        }
    }

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
