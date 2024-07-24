package mobex.User;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import mobex.Token.ResetPasswordToken;
import mobex.Token.ResetPasswordTokenRepository;
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
    private final EmailService emailService;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                       EmailValidator emailValidator, NameValidator nameValidator,
                       PasswordValidator passwordValidator, EmailService emailService, ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailValidator = emailValidator;
        this.nameValidator = nameValidator;
        this.passwordValidator = passwordValidator;
        this.emailService = emailService;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    @Transactional
    public User registerUser(User user){
        String userFirstName = user.getFirstname();
        if(nameValidator.isValidName(userFirstName)){
            throw new RuntimeException("First name is not valid " + userFirstName);
        }

        String userLastName = user.getLastname();
        if(nameValidator.isValidName(userLastName)){
            throw new RuntimeException("Last name is not valid " + userLastName);
        }

        String userEmail = user.getEmail();
        if(emailValidator.isValidEmail(userEmail)) {
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

    @Transactional
    public void forgotPassword(String email) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            ResetPasswordToken token = new ResetPasswordToken(userOptional.get());
            resetPasswordTokenRepository.save(token);
            emailService.sendSetPasswordEmail(email, token.getToken());
        }
        else throw new RuntimeException("Email not found");
    }

    @Transactional
    public void setPassword(String password, String token){
        Optional<ResetPasswordToken> tokenOptional = resetPasswordTokenRepository.findByToken(token);
        if(tokenOptional.isPresent()){
            ResetPasswordToken resetPasswordToken = tokenOptional.get();
            if(resetPasswordToken.isValid()){
                User user = tokenOptional.get().getUser();
                if(passwordValidator.isValidPassword(password)){
                    if(!passwordEncoder.matches(password,user.getPassword())){
                        String hashedPassword = passwordEncoder.encode(password);
                        user.setPassword(hashedPassword);
                        resetPasswordTokenRepository.delete(resetPasswordToken);
                        userRepository.save(user);
                    }
                    else throw new RuntimeException("New password can't match old password");
                }
                else throw new RuntimeException("Invalid password format");
            }
            else throw new RuntimeException("Reset password token is expired");
        }
        else throw new RuntimeException("Invalid email address");
    }
}
