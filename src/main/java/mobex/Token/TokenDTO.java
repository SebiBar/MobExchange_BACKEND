package mobex.Token;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}
