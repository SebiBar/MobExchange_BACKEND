package mobex.Token;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mobex.User.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for managing user authentication and authorization.")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns the authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully and token created"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid data."),
            @ApiResponse(responseCode = "401", description = "Registration failed due to some authentication-related issue (e.g., username already taken, invalid data).")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userData) {
        try {
            User user = userService.registerUser(userData);
            TokenDTO tokenDTO = authService.createTokenReturnDTO(user);
            return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns the authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User authenticated successfully and token created"),
            @ApiResponse(responseCode = "401", description = "Login failed due to incorrect credentials.")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        try {
            User user = userService.loginUser(userDTO);
            TokenDTO tokenDTO = authService.createTokenReturnDTO(user);
            return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "User logout", description = "Logs out the user and invalidates the provided access token.")
    @Parameter(name = "Authorization", description = "The access token to be invalidated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged out and token invalidated."),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Token not valid (e.g., already logged out or invalid token format).")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken){
        try {
            authService.deleteToken(accessToken);
            return new ResponseEntity<>("Token deleted", HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Get user details", description = "Retrieves details of the authenticated user.")
    @Parameter(name = "Authorization", description = "The access token of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - token is missing or invalid.")
    })
    @GetMapping("/getUserDetails")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String accessToken){
        try {
            UserDetailsDTO userDetailsDTO = authService.getUserDetails(accessToken);
            return new ResponseEntity<>(userDetailsDTO, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Get new refresh token", description = "Replaces the old refresh token with a new one.")
    @Parameter(name = "Authorization", description = "The refresh token to be replaced")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New refresh token generated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Provided refresh token is invalid")
    })
    @PostMapping("/getNewRefreshToken")
    public ResponseEntity<?> getNewRefreshToken(@RequestHeader("Authorization") String refreshToken){
        try {
            TokenDTO tokenDTO = authService.replaceOldToken(refreshToken);
            return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Verify access token", description = "Verifies if the provided access token is valid.")
    @Parameter(name = "Authorization", description = "The access token to be verified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or expired token")
    })
    @GetMapping("/verifyAccessToken")
    public ResponseEntity<?> verifyAccessToken(@RequestHeader("Authorization") String accessToken){
        try {
            Token token = authService.getTokenByAccessToken(accessToken);
            if (token.isValid())
                return new ResponseEntity<>("Valid token", HttpStatus.OK);
            else
                return new ResponseEntity<>("Expired token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Verify refresh token", description = "Verifies if the provided refresh token is valid.")
    @Parameter(name = "Authorization", description = "The refresh token to be verified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid refresh token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or expired refresh token")
    })
    @GetMapping("/verifyRefreshToken")
    public ResponseEntity<?> verifyRefreshToken(@RequestHeader("Authorization") String refreshToken){
        try {
            Token token = authService.getTokenByRefreshToken(refreshToken);
            if (token.isValidRefreshToken())
                return new ResponseEntity<>("Valid token", HttpStatus.OK);
            else
                return new ResponseEntity<>("Expired token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Change a user's password", description = "Verifies if an access token is valid, changes old password with a new one")
    @Parameter(name = "Authorization", description = "The access token to be verified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or expired access token / old password doesn't match / invalid new password format")

    })
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String accessToken,
                                            @RequestBody PasswordDTO passwordDTO){
        try {
            Token token = authService.getTokenByAccessToken(accessToken);
            if(token.isValid()){
                User user = authService.getUserByAccessToken(accessToken);
                userService.changePassword(user, passwordDTO);
                return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Expired access token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @Operation(summary = "Forgot password", description = "Sends an email to the user with a link to reset the password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "401", description = "Email does not exist in the database")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailDTO emailDTO){
        try{
            userService.forgotPassword(emailDTO.getEmail());
            return new ResponseEntity<>("Sent email successfully", HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Set password", description = "The user sets a new password from the email forgot password link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password set successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or same password")
    })
    @PostMapping("/set-password/")
    public ResponseEntity<?> setPassword(@RequestBody SetPasswordDTO setPasswordDTO, @RequestParam String token){
        try{
            userService.setPassword(setPasswordDTO.getNewPassword(), token);
            return new ResponseEntity<>("New password set successfully",HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }
}
