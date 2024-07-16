package mobex.User;

import jakarta.transaction.Transactional;
import mobex.Token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private TokenService tokenService;
    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public User registerUser(User user){
        User newUser = userRepository.save(user);
        tokenService.createToken(user);
        return newUser;
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
