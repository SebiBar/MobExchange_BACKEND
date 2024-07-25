package mobex.ExchangeRate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mobex.Token.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.NotActiveException;


// TODO: handle NotActiveExceptions from accessTokens

@RestController
@RequestMapping("/exchange-rates")
@Tag(name = "Exchange Rate Management", description = "Endpoints for retrieving and converting exchange rates.")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;
    private final AuthService authService;

    public ExchangeRateController(ExchangeRateService exchangeRateService, AuthService authService) {
        this.exchangeRateService = exchangeRateService;
        this.authService = authService;
    }

    // Get latest exchange rates for a defined currency
    @Operation(summary = "Get latest exchange rates for a defined currency",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully", responseCode = "200"),
                    @ApiResponse(description = "Expired token", responseCode = "401"),
                    @ApiResponse(description = "Invalid token", responseCode = "400")

            })
    @GetMapping("/latest/{baseCurrency}")
    public ResponseEntity<?> getLatestRates(
            @RequestHeader("Authorization") String accessToken,
            @Parameter(description = "The base currency for which to get the latest exchange rates")
            @PathVariable String baseCurrency) {
        try{
            authService.getValidTokenByAccessToken(accessToken);
            return new ResponseEntity<>(exchangeRateService.getLatestRates(baseCurrency), HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get exchange rates for a specific date and currency
    @Operation(summary = "Get exchange rates for a specific date and currency",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully", responseCode = "200"),
                    @ApiResponse(description = "Expired token", responseCode = "401"),
                    @ApiResponse(description = "Invalid Token", responseCode = "400")
            })
    @GetMapping("/{date}/{baseCurrency}")
    public ResponseEntity<?> getExchangeRatesForDate(
            @RequestHeader("Authorization") String accessToken,
            @Parameter(description = "The date for which to get the exchange rates in YYYY-MM-DD format")
            @PathVariable String date,
            @Parameter(description = "The base currency for which to get the exchange rates")
            @PathVariable String baseCurrency) {
        try{
            authService.getValidTokenByAccessToken(accessToken);
            return new ResponseEntity<>(exchangeRateService.getRatesForDate(date, baseCurrency), HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // All rates for a specific period for a defined currency
    @Operation(summary = "Get all rates for a specific period for a defined currency",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully", responseCode = "200"),
                    @ApiResponse(description = "Expired token", responseCode = "401"),
                    @ApiResponse(description = "Invalid Token", responseCode = "400")
            })
    @GetMapping("/period/{startDate}/{endDate}/{baseCurrency}")
    public ResponseEntity<?> getExchangeRatesForPeriod(
            @RequestHeader("Authorization") String accessToken,
            @Parameter(description = "The start date of the period in YYYY-MM-DD format")
            @PathVariable String startDate,
            @Parameter(description = "The end date of the period in YYYY-MM-DD format")
            @PathVariable String endDate,
            @Parameter(description = "The base currency for which to get the exchange rates")
            @PathVariable String baseCurrency) {
        try{
            authService.getValidTokenByAccessToken(accessToken);
            return new ResponseEntity<>(exchangeRateService.getAllRatesForPeriod(startDate, endDate, baseCurrency), HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Historical exchange rates between two currencies for a defined period
    @Operation(summary = "Get historical exchange rates between two currencies for a defined period",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully", responseCode = "200"),
                    @ApiResponse(description = "Expired token", responseCode = "401"),
                    @ApiResponse(description = "Invalid Token", responseCode = "400")
            })
    @GetMapping("/historical/{startDate}/{endDate}/{baseCurrency}/{targetCurrency}")
    public ResponseEntity<?> getHistoricalRatesBetweenCurrencies(
            @RequestHeader("Authorization") String accessToken,
            @Parameter(description = "The start date of the period in YYYY-MM-DD format")
            @PathVariable String startDate,
            @Parameter(description = "The end date of the period in YYYY-MM-DD format")
            @PathVariable String endDate,
            @Parameter(description = "The base currency for which to get the exchange rates")
            @PathVariable String baseCurrency,
            @Parameter(description = "The target currency for which to get the exchange rates")
            @PathVariable String targetCurrency) {
        try{
            authService.getValidTokenByAccessToken(accessToken);
            return new ResponseEntity<>(
                    exchangeRateService.getHistoricalRatesForCurrencies(startDate, endDate, baseCurrency, targetCurrency),
                    HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    // Endpoint to get historical rates for two currencies from a specific date until today
    @Operation(summary = "Get historical rates for two currencies from a specific date until today",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully", responseCode = "200"),
                    @ApiResponse(description = "Expired token", responseCode = "401"),
                    @ApiResponse(description = "Invalid Token", responseCode = "400")
            })
    @GetMapping("/historical/{startDate}/{baseCurrency}/{targetCurrency}")
    public ResponseEntity<?> getHistoricalRatesBetweenCurrenciesFromToNow(
            @RequestHeader("Authorization") String accessToken,
            @Parameter(description = "The start date from which to get the exchange rates in YYYY-MM-DD format")
            @PathVariable String startDate,
            @Parameter(description = "The base currency for which to get the exchange rates")
            @PathVariable String baseCurrency,
            @Parameter(description = "The target currency for which to get the exchange rates")
            @PathVariable String targetCurrency) {
        try{
            authService.getValidTokenByAccessToken(accessToken);
            return new ResponseEntity<>(
                    exchangeRateService.getHistoricalRatesFromToNow(startDate, baseCurrency, targetCurrency),
                    HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    // Endpoint to convert currency from one to another using the latest rates
    @Operation(summary = "Convert currency from one to another using the latest rates",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully", responseCode = "200"),
                    @ApiResponse(description = "Expired token", responseCode = "401"),
                    @ApiResponse(description = "Invalid Token", responseCode = "400")
            })
    @GetMapping("/convert")
    public ResponseEntity<?> convertCurrency(
            @RequestHeader("Authorization") String accessToken,
            @Parameter(description = "The amount of currency to convert")
            @RequestParam double amount,
            @Parameter(description = "The currency from which to convert")
            @RequestParam String from,
            @Parameter(description = "The currency to which to convert")
            @RequestParam String to) {
        try{
            authService.getValidTokenByAccessToken(accessToken);
            return new ResponseEntity<>(exchangeRateService.convertCurrency(amount, from, to), HttpStatus.OK);
        }catch (NotActiveException e){
            return new ResponseEntity<>("Access Token expired", HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
