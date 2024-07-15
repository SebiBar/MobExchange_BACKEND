package mobex.AppUser;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    private final EmailValidator emailValidator;

    @Autowired
    public AppUserServiceImpl(EmailValidator emailValidator){
        this.emailValidator = emailValidator;
    }
    @Transactional
    @Override
    public AppUser register (AppUser appUser) {
        try {
            String userEmail = appUser.getEmail();
            if(!emailValidator.isValidEmail(userEmail))
                throw new EmailNotValidException("Email is not valid: " + userEmail);
            return appUserRepository.save(appUser);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }

    }
}
