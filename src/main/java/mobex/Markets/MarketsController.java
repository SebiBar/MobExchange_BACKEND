package mobex.Markets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import mobex.Token.AuthService;

import java.io.IOException;
import java.io.NotActiveException;

@RestController
@RequestMapping("/markets")
public class MarketsController {

    private final MarketsService marketsService;
    private final AuthService authService;

    @Autowired
    public MarketsController(MarketsService marketsService, AuthService authService) {
        this.marketsService = marketsService;
        this.authService = authService;
    }

    

    @Operation(summary = "Get World Indices",  
            description = "Retrieves the latest data for various world indices from the local file.",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved world indices data."),  
                    @ApiResponse(responseCode = "401", description = "Expired token"),  
                    @ApiResponse(responseCode = "400", description = "Invalid Token"),  
                    @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),  
                    @ApiResponse(responseCode = "500", description = "Internal server error.")  
            })  
    @GetMapping("/world-indices")  
    public ResponseEntity<String> getWorldIndices(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre indicii de pe piețele mondiale  
            String response = marketsService.fetchWorldIndices();  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }



    @Operation(summary = "Get Futures",  
            description = "Retrieves the latest data for various futures from the local file.",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved futures data."),  
                    @ApiResponse(responseCode = "401", description = "Expired token"),  
                    @ApiResponse(responseCode = "400", description = "Invalid Token"),  
                    @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),  
                    @ApiResponse(responseCode = "500", description = "Internal server error.")  
            })  
    @GetMapping("/commodities")  
    public ResponseEntity<String> getFutures(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre futures  
            String response = marketsService.fetchFutures();  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }




    @Operation(summary = "Get Bonds",  
            description = "Retrieves the latest data for various bonds from the local file.",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved bonds data."),  
                    @ApiResponse(responseCode = "401", description = "Expired token"),  
                    @ApiResponse(responseCode = "400", description = "Invalid Token"),  
                    @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),  
                    @ApiResponse(responseCode = "500", description = "Internal server error.")  
            })  
    @GetMapping("/bonds")  
    public ResponseEntity<String> getBonds(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre bonds  
            String response = marketsService.fetchBonds();  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }





    @Operation(summary = "Get Stock Chart Data",  
            description = "Retrieves the latest chart data (hystorical data) for a specific stock symbol.",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved stock chart data."),  
                    @ApiResponse(responseCode = "401", description = "Invalid API key or expired token."),  
                    @ApiResponse(responseCode = "403", description = "You are not subscribed to this API."),  
                    @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),  
                    @ApiResponse(responseCode = "500", description = "Internal server error.")  
            })  
    @GetMapping("/stock-chart")  
    public ResponseEntity<String> getStockChartData(  
            @RequestHeader("Authorization") String accessToken,  
            @RequestParam String symbol,  
            @RequestParam String range,  
            @RequestParam String interval) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre graficul acțiunilor  
            String response = marketsService.fetchStockChartData(symbol, range, interval);  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching stock chart data: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }
}