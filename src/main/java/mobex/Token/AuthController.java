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

import java.io.NotActiveException;

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
            @ApiResponse(responseCode = "200", description = "User registered successfully and token created"),
            @ApiResponse(responseCode = "400", description = "Registration failed due to some authentication-related issue (e.g., username already taken, invalid data).")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userData){
        try {
            User user = userService.registerUser(userData);
            TokenDTO tokenDTO = authService.createTokenReturnDTO(user);
            return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns the authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully and token created"),
            @ApiResponse(responseCode = "400", description = "Login failed due to incorrect credentials.")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        try {
            User user = userService.loginUser(userDTO);
            TokenDTO tokenDTO = authService.createTokenReturnDTO(user);
            return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "User logout", description = "Logs out the user and invalidates the provided access token.")
    @Parameter(name = "Authorization", description = "The access token to be invalidated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged out and token invalidated."),
            @ApiResponse(responseCode = "401", description = "Access token expired"),
            @ApiResponse(responseCode = "400", description = "Invalid token")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken){
        try {
            authService.deleteValidToken(accessToken);
            return new ResponseEntity<>("Token deleted", HttpStatus.OK);
        }catch(NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        }catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get user details", description = "Retrieves details of the authenticated user.")
    @Parameter(name = "Authorization", description = "The access token of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Access token expired"),
            @ApiResponse(responseCode = "400", description = "Invalid access token")
    })
    @GetMapping("/getUserDetails")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String accessToken){
        try {
            UserDetailsDTO userDetailsDTO = authService.getValidUserDetails(accessToken);
            return new ResponseEntity<>(userDetailsDTO, HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get new refresh token", description = "Replaces the old refresh token with a new one.")
    @Parameter(name = "Authorization", description = "The refresh token to be replaced")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New refresh token generated successfully"),
            @ApiResponse(responseCode = "401", description = "Refresh token expired"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token")
    })
    @PostMapping("/getNewAccessToken")
    public ResponseEntity<?> getNewAccessToken(@RequestHeader("Authorization") String refreshToken){
        try {
            TokenDTO tokenDTO = authService.replaceValidOldToken(refreshToken);
            return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Verify access token", description = "Verifies if the provided access token is valid.")
    @Parameter(name = "Authorization", description = "The access token to be verified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid token"),
            @ApiResponse(responseCode = "401", description = "Access token expired"),
            @ApiResponse(responseCode = "400", description = "Invalid token")
    })
    @GetMapping("/verifyAccessToken")
    public ResponseEntity<?> verifyAccessToken(@RequestHeader("Authorization") String accessToken){
        try {
            authService.getValidTokenByAccessToken(accessToken);
            return new ResponseEntity<>("Valid access token", HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Change a user's password", description = "Verifies if an access token is valid, changes old password with a new one")
    @Parameter(name = "Authorization", description = "The access token to be verified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "401", description = "Token has expired"),
            @ApiResponse(responseCode = "400", description = "Old password doesn't match / invalid new password format")
    })
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String accessToken,
                                            @RequestBody PasswordDTO passwordDTO){
        try {
            User user = authService.getUserByValidAccessToken(accessToken);
            userService.changePassword(user, passwordDTO);
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @Operation(summary = "Forgot password", description = "Sends an email to the user with a link to reset the password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Email does not exist in the database")
    })
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailDTO emailDTO){
        try{
            userService.forgotPassword(emailDTO.getEmail());
            return new ResponseEntity<>("Sent email successfully", HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Set password", description = "The user sets a new password from the email forgot password link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password set successfully"),
            @ApiResponse(responseCode = "401", description = "Reset token expired"),
            @ApiResponse(responseCode = "400", description = "Invalid or same password")
    })
    @PostMapping("/setPassword/")
    public ResponseEntity<?> setPassword(@RequestBody SetPasswordDTO setPasswordDTO, @RequestParam String token){
        try{
            userService.setPassword(setPasswordDTO.getNewPassword(), token);
            return new ResponseEntity<>("New password set successfully",HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Reset token expired", HttpStatus.UNAUTHORIZED);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
