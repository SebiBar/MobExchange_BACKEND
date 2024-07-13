package mobex.AppUser;

import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String hello(){
        return "Hello World";
    }

    //@PostMapping("/register")
    //public ResponseEntity<?>
}
