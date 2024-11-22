package mobex.Config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.*;
// import org.springframework.lang.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;


@Configuration
public class MvcConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS")
                .allowedOrigins("http://localhost:5173") // http://localhost:5173
                .allowedHeaders("accessToken", "Authorization", "content-type")
                .exposedHeaders("accessToken", "Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }
}



// package mobex.Config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import org.springframework.lang.NonNull;

// @Configuration
// public class MvcConfig implements WebMvcConfigurer {

//     @Override
//     public void addCorsMappings(@NonNull CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS")
//                 .allowedOrigins("http://localhost:5173")
//                 .allowedHeaders("*")
//                 .exposedHeaders("Authorization")
//                 .allowCredentials(true)
//                 .maxAge(3600L);
//     }
// }