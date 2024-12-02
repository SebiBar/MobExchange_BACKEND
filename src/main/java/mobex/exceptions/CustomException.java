package mobex.exceptions; // Asigură-te că pachetul este corect

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final HttpStatus statusCode;

    public CustomException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }


    public HttpStatus getStatusCode() {
        return statusCode;
    }
}