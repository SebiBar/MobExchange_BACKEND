package mobex.ExchangeRate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchange-rates")
@Tag(name = "Exchange Rate Management", description = "Endpoints for retrieving and converting exchange rates.")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    // Get latest exchange rates for a defined currency
    @Operation(summary = "Get latest exchange rates for a defined currency",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403")
            })
    @GetMapping("/latest/{baseCurrency}")
    public String getLatestRates(
            @Parameter(description = "The base currency for which to get the latest exchange rates")
            @PathVariable String baseCurrency) {
        return exchangeRateService.getLatestRates(baseCurrency);
    }

    // Get exchange rates for a specific date and currency
    @Operation(summary = "Get exchange rates for a specific date and currency",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403")
            })
    @GetMapping("/{date}/{baseCurrency}")
    public String getExchangeRatesForDate(
            @Parameter(description = "The date for which to get the exchange rates in YYYY-MM-DD format")
            @PathVariable String date,
            @Parameter(description = "The base currency for which to get the exchange rates")
            @PathVariable String baseCurrency) {
        return exchangeRateService.getRatesForDate(date, baseCurrency);
    }

    // All rates for a specific period for a defined currency
    @Operation(summary = "Get all rates for a specific period for a defined currency",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403")
            })
    @GetMapping("/period/{startDate}/{endDate}/{baseCurrency}")
    public String getExchangeRatesForPeriod(
            @Parameter(description = "The start date of the period in YYYY-MM-DD format")
            @PathVariable String startDate,
            @Parameter(description = "The end date of the period in YYYY-MM-DD format")
            @PathVariable String endDate,
            @Parameter(description = "The base currency for which to get the exchange rates")
            @PathVariable String baseCurrency) {
        return exchangeRateService.getAllRatesForPeriod(startDate, endDate, baseCurrency);
    }

    // Historical exchange rates between two currencies for a defined period
    @Operation(summary = "Get historical exchange rates between two currencies for a defined period",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403")
            })
    @GetMapping("/historical/{startDate}/{endDate}/{baseCurrency}/{targetCurrency}")
    public String getHistoricalRatesBetweenCurrencies(
            @Parameter(description = "The start date of the period in YYYY-MM-DD format")
            @PathVariable String startDate,
            @Parameter(description = "The end date of the period in YYYY-MM-DD format")
            @PathVariable String endDate,
            @Parameter(description = "The base currency for which to get the exchange rates")
            @PathVariable String baseCurrency,
            @Parameter(description = "The target currency for which to get the exchange rates")
            @PathVariable String targetCurrency) {
        return exchangeRateService.getHistoricalRatesForCurrencies(startDate, endDate, baseCurrency, targetCurrency);
    }

    // Endpoint to get historical rates for two currencies from a specific date until today
    @Operation(summary = "Get historical rates for two currencies from a specific date until today",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403")
            })
    @GetMapping("/historical/{startDate}/{baseCurrency}/{targetCurrency}")
    public String getHistoricalRatesBetweenCurrenciesFromToNow(
            @Parameter(description = "The start date from which to get the exchange rates in YYYY-MM-DD format")
            @PathVariable String startDate,
            @Parameter(description = "The base currency for which to get the exchange rates")
            @PathVariable String baseCurrency,
            @Parameter(description = "The target currency for which to get the exchange rates")
            @PathVariable String targetCurrency) {
        return exchangeRateService.getHistoricalRatesFromToNow(startDate, baseCurrency, targetCurrency);
    }

    // Endpoint to convert currency from one to another using the latest rates
    @Operation(summary = "Convert currency from one to another using the latest rates",
            responses = {
                    @ApiResponse(description = "Exchange rates retrieved successfully",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403")
            })
    @GetMapping("/convert")
    public Double convertCurrency(
            @Parameter(description = "The amount of currency to convert")
            @RequestParam double amount,
            @Parameter(description = "The currency from which to convert")
            @RequestParam String from,
            @Parameter(description = "The currency to which to convert")
            @RequestParam String to) {
        return exchangeRateService.convertCurrency(amount, from, to);
    }
}
