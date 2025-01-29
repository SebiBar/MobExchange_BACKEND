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
            String response = marketsService.fetchData("world_indices.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-quote?symbols=%5EGSPC%2C%5EDJI%2C%5EIXIC%2C%5ENYA%2C%5EXAX%2C%5EBUK100P%2C%5ERUT%2C%5EVIX%2C%5EFTSE%2C%5EGDAXI%2C%5EFCHI%2C%5ESTOXX50E%2C%5EN100%2C%5EBFX%2CMOEX.ME%2CN225%2C%5EHSI%2C00001.SS%2C99001.SZ%2C%5ESTI%2C%5EAXJO%2C%5EAORD%2C%5BBSESN%2C%5EJKSE%2C%5EKLSE%2C%5ENZ50%2C%5EKS11%2C%5ETWII%2C%5EGSPTSE%2C%5EBVSP%2C%5EMXX%2C%5EIPSA%2C%5EMERV%2C%5ETA125.TA%2C%5ECASE30%2C%5EJN0U.JO%2CDX-Y.NYB%2C%5E125904-USD-STRD%2C%5EXDB%2C%5EXDE%2C000001.SS%2C%5EN225%2C%5EXDN%2C%5EXDA");  
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
            String response = marketsService.fetchData("futures.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-quote?symbols=ES%3DF%2CYM%3DF%2CNQ%3DF%2CRTY%3DF%2CZB%3DF%2CZN%3DF%2CZF%3DF%2CZT%3DF%2CGC%3DF%2CMGC%3DF%2CSI%3DF%2CSIL%3DF%2CPL%3DF%2CHG%3DF%2CPA%3DF%2CCL%3DF%2CHO%3DF%2CNG%3DF%2CRB%3DF%2CBZ%3DF%2CB0%3DF%2CZC%3DF%2CZO%3DF%2CKE%3DF%2CZR%3DF%2CZM%3DF%2CZL%3DF%2CZS%3DF%2CGF%3DF%2CHE%3DF%2CLE%3DF%2CCC%3DF%2CKC%3DF%2CCT%3DF%2CLBS%3DF%2COJ%3DF%2CSB%3DF");  
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
            String response = marketsService.fetchData("bonds.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-quote?symbols=%5EIRX%2C%5EFVX%2C%5ETNX%2C%5ETYX%2C2YY%3DF%2CZN%3DF");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }








    @Operation(summary = "Get Currencies",  
            description = "Retrieves the latest data for various Currencies from the local file.",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Currencies data."),  
                    @ApiResponse(responseCode = "401", description = "Expired token"),  
                    @ApiResponse(responseCode = "400", description = "Invalid Token"),  
                    @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),  
                    @ApiResponse(responseCode = "500", description = "Internal server error.")  
            })  
    @GetMapping("/currencies")  
    public ResponseEntity<String> getCurrencies(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre bonds  
            String response = marketsService.fetchData("currencies.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-quote?symbols=EURUSD%3DX%2CJPY%3DX%2CGBPUSD%3DX%2CAUDUSD%3DX%2CNZDUSD%3DX%2CEURJPY%3DX%2CGBPJPY%3DX%2CEURGBP%3DX%2CEURCAD%3DX%2CEURSEK%3DX%2CEURCHF%3DX%2CEURHUF%3DX%2CCNY%3DX%2CHKD%3DX%2C SGD%3DX%2CINR%3DX%2CMXN%3DX%2CPHP%3DX%2CIDR%3DX%2CTHB%3DX%2CMYR%3DX%2CZAR%3DX%2CRUB%3DX");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }






    @Operation(summary = "Get Options",  
            description = "Retrieves the latest data for various Options from the local file.",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Options data."),  
                    @ApiResponse(responseCode = "401", description = "Expired token"),  
                    @ApiResponse(responseCode = "400", description = "Invalid Token"),  
                    @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),  
                    @ApiResponse(responseCode = "500", description = "Internal server error.")  
            })  
    @GetMapping("/options/most-active")  
    public ResponseEntity<String> getOptions(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("options_most_active.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-most-actives?quote_type=OPTIONS&offset=0&count=25&region=US&language=en-US");  
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