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

// import org.json.JSONArray;  
// import org.json.JSONObject;

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



    @Operation(summary = "Get General Data for 1 Asset",  
            description = "Retrieves the latest data for 1 Asset from Yahoo Finance.",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved data."),  
                    @ApiResponse(responseCode = "401", description = "Expired token"),  
                    @ApiResponse(responseCode = "400", description = "Invalid Token"),  
                    @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests."),  
                    @ApiResponse(responseCode = "500", description = "Internal server error.")  
            })  
    @GetMapping("/options/get-general-data-for-1-asset")  
    public ResponseEntity<String> getGeneralDataFor1Asset(  
            @RequestHeader("Authorization") String accessToken,  
            @RequestParam("symbol") String symbol) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Construim URL-ul pentru API-ul Yahoo Finance  
            String url = String.format("https://yahoo-finance166.p.rapidapi.com/api/market/get-quote?symbols=%s", symbol);  
            
            // Facem request către API-ul Yahoo Finance  
            String response = marketsService.fetchDataFor1Asset(url);  
            
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching data: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
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

    @GetMapping("/options/gainers")  
    public ResponseEntity<String> getOptionsGainers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("options_gainers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-day-gainers?offset=0&region=US&count=25&language=en-US&quote_type=OPTIONS");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/options/losers")  
    public ResponseEntity<String> getOptionsLosers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("options_losers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-day-losers?offset=0&language=en-US&region=US&count=25&quote_type=OPTIONS");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/options/highest-implied-volatility")  
    public ResponseEntity<String> getOptionsHighestImpliedVolatility(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("options_highest_implied_volatility.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-highest-implied-volatility?language=en-US&region=US&offset=0&quote_type=OPTIONS&count=25");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/options/highest-open-interest")  
    public ResponseEntity<String> getOptionsHighestOpenInterest(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("options_highest_open_interest.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-highest-open-interest?count=25&language=en-US&region=US&quote_type=OPTIONS&offset=0");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }






    // **********
    //  STOCKS 
    // **********

    @Operation(summary = "Get Stocks",  
            description = "Retrieves the latest data for various Stocks from the local file.",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Stocks data."),  
                    @ApiResponse(responseCode = "401", description = "Expired token"),  
                    @ApiResponse(responseCode = "400", description = "Invalid Token"),  
                    @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),  
                    @ApiResponse(responseCode = "500", description = "Internal server error.")  
            })  
    @GetMapping("/stocks/most-active")  
    public ResponseEntity<String> getStocks(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("stocks_most_active.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-most-actives?quote_type=EQUITY&offset=0&count=25&region=US&language=en-US");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/stocks/trending")  
    public ResponseEntity<String> getStocksTrending(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("stocks_trending.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-trending?quote_type=EQUITY&offset=0&count=25&region=US&language=en-US");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }


    @GetMapping("/stocks/gainers")  
    public ResponseEntity<String> getStocksGainers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("stocks_gainers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-day-gainers?offset=0&region=US&count=25&language=en-US&quote_type=EQUITY");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/stocks/losers")  
    public ResponseEntity<String> getStocksLosers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("stocks_losers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-day-losers?offset=0&language=en-US&region=US&count=25&quote_type=EQUITY");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }


    @GetMapping("/stocks/52-wk-gainers")  
    public ResponseEntity<String> getStocks52WeekGainers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("stocks_52WkGainers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/52-wk-gainers?offset=0&region=US&count=25&language=en-US&quote_type=EQUITY");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/stocks/52-wk-losers")  
    public ResponseEntity<String> getStocks52WeekLosers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Options  
            String response = marketsService.fetchData("stocks_52WkLosers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/52-wk-losers?offset=0&count=25&quote_type=EQUITY&language=en-US&region=US");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }







    // **********
    //  CRYPTO 
    // **********

    @Operation(summary = "Get Crypto",  
            description = "Retrieves the latest data for various Crypto from the local file.",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Crypto data."),  
                    @ApiResponse(responseCode = "401", description = "Expired token"),  
                    @ApiResponse(responseCode = "400", description = "Invalid Token"),  
                    @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),  
                    @ApiResponse(responseCode = "500", description = "Internal server error.")  
            })  
    @GetMapping("/crypto/most-active")  
    public ResponseEntity<String> getCrypto(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Crypto  
            String response = marketsService.fetchData("crypto_most_active.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-most-actives?quote_type=CRYPTOCURRENCIES&offset=0&count=25&region=US&language=en-US");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }



    @GetMapping("/crypto/gainers")  
    public ResponseEntity<String> getCryptoGainers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Crypto  
            String response = marketsService.fetchData("crypto_gainers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-day-gainers?offset=0&region=US&count=25&language=en-US&quote_type=CRYPTOCURRENCIES");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/crypto/losers")  
    public ResponseEntity<String> getCryptoLosers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Crypto  
            String response = marketsService.fetchData("crypto_losers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-day-losers?offset=0&language=en-US&region=US&count=25&quote_type=CRYPTOCURRENCIES");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }





    // **********
    //  ETFS 
    // **********

    @Operation(summary = "Get ETFS",  
            description = "Retrieves the latest data for various ETFS from the local file.",  
            responses = {  
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved ETFS data."),  
                    @ApiResponse(responseCode = "401", description = "Expired token"),  
                    @ApiResponse(responseCode = "400", description = "Invalid Token"),  
                    @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),  
                    @ApiResponse(responseCode = "500", description = "Internal server error.")  
            })  
    @GetMapping("/etfs/most-active")  
    public ResponseEntity<String> getEtfs(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre ETFS  
            String response = marketsService.fetchData("etfs_most_active.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-most-actives?quote_type=ETFS&offset=0&count=25&region=US&language=en-US");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }



    @GetMapping("/etfs/gainers")  
    public ResponseEntity<String> getEtfsGainers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre ETFS  
            String response = marketsService.fetchData("etfs_gainers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-day-gainers?offset=0&region=US&count=25&language=en-US&quote_type=ETFS");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/etfs/losers")  
    public ResponseEntity<String> getEtfsLosers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre ETFS  
            String response = marketsService.fetchData("etfs_losers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-day-losers?offset=0&language=en-US&region=US&count=25&quote_type=ETFS");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/etfs/top-performing")  
    public ResponseEntity<String> getEtfsTopPerforming(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre ETFS  
            String response = marketsService.fetchData("etfs_top_performing.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-top-performing?count=25&region=US&language=en-US&quote_type=ETFS&offset=0");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/etfs/trending")  
    public ResponseEntity<String> getEtfsTrending(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre ETFS  
            String response = marketsService.fetchData("etfs_trending.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-trending?region=US&language=en-US&quote_type=ETF&count=25&offset=0");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/etfs/best-historical-performance")  
    public ResponseEntity<String> getEtfsBestHistoricalPerformance(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre ETFS  
            String response = marketsService.fetchData("etfs_best_historical_performance.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-best-historical-performance?region=US&count=25&offset=0&quote_type=ETFS&language=en-US");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }




    // **************
    //  Mutual Funds 
    // **************
    @Operation(summary = "Get Mutual Funds",  
    description = "Retrieves the latest data for various Mutual Funds from the local file.",  
    responses = {  
            @ApiResponse(responseCode = "200", description = "Successfully retrieved Mutual Funds data."),  
            @ApiResponse(responseCode = "401", description = "Expired token"),  
            @ApiResponse(responseCode = "400", description = "Invalid Token"),  
            @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),  
            @ApiResponse(responseCode = "500", description = "Internal server error.")  
    }) 
    @GetMapping("/mutual-funds/gainers")  
    public ResponseEntity<String> getMutualFundsGainers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre Mutual Funds   
            String response = marketsService.fetchData("mutual_funds_gainers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-day-gainers?offset=0&region=US&count=25&language=en-US&quote_type=MUTUAL_FUNDS");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/mutual-funds/losers")  
    public ResponseEntity<String> getMutualFundsLosers(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre mutual funds  
            String response = marketsService.fetchData("mutual_funds_losers.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-day-losers?offset=0&language=en-US&region=US&count=25&quote_type=MUTUAL_FUNDS");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }

    @GetMapping("/mutual-funds/top-performing")  
    public ResponseEntity<String> getMutualFundsTopPerforming(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre mutual funds   
            String response = marketsService.fetchData("mutual_funds_top_performing.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-top-performing?count=25&region=US&language=en-US&quote_type=MUTUAL_FUNDS&offset=0");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }
    @GetMapping("/mutual-funds/best-historical-performance")  
    public ResponseEntity<String> getEtfsMutualFundsBestHistoricalPerformance(  
            @RequestHeader("Authorization") String accessToken) {  
        try {  
            // Verificăm validitatea token-ului  
            authService.getValidTokenByAccessToken(accessToken);  
            
            // Obținem datele despre mutual funds  
            String response = marketsService.fetchData("mutual_funds_best_historical_performance.json", "https://yahoo-finance166.p.rapidapi.com/api/market/get-best-historical-performance?region=US&count=25&offset=0&quote_type=MUTUAL_FUNDS&language=en-US");  
            return ResponseEntity.ok(response);  
        } catch (NotActiveException e) {  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token expired");  
        } catch (IOException e) {  
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing to file: " + e.getMessage());  
        } catch (Exception e) {  
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }  
    }



    // **********
    //  CHART
    // **********

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