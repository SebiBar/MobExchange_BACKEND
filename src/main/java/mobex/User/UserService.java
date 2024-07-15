package mobex.User;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<UserDTO> findByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            User foundUser = user.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(foundUser.getEmail());
            return Optional.of(userDTO);
        }
        else{
            return Optional.empty();
        }
    }
    @Transactional
    public User registerUser(User userDto){
        userRepository.save(userDto);
        return userDto;
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
