package mobex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv; 

@SpringBootApplication
public class MobExchangeApplication {

	public static void main(String[] args) {
		// Încarcă variabilele din fișierul .env  
        Dotenv dotenv = Dotenv.load(); 

		// Accesează variabilele de mediu  
        String newsApiKey = dotenv.get("NEWS_API_KEY");  
        String rapidApiKey = dotenv.get("RAPID_API_KEY");

		// Setează variabilele ca proprietăți de sistem  
        System.setProperty("NEWS_API_KEY", newsApiKey);  
        System.setProperty("RAPID_API_KEY", rapidApiKey);


		SpringApplication.run(MobExchangeApplication.class, args);
	}

}
