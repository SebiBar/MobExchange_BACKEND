package mobex.ExchangeRate;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/exchange-rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    // Get latest exchange rates for a defined currency
    @GetMapping("/latest/{baseCurrency}")
    public String getLatestRates(@PathVariable String baseCurrency) {
        return exchangeRateService.getLatestRates(baseCurrency);
    }

    // Get exchange rates for a specific date and currency
    @GetMapping("/{baseCurrency}/{date}")
    public String getExchangeRatesForDate(
            @PathVariable String date,
            @PathVariable String baseCurrency){
        return exchangeRateService.getRatesForDate(date, baseCurrency);
    }

    // All rates for a specific period for a defined currency
    @GetMapping("/period/{startDate}/{endDate}/{baseCurrency}")
    public String getExchangeRatesForPeriod(@PathVariable String startDate, @PathVariable String endDate,@PathVariable String baseCurrency) {
        return exchangeRateService.getAllRatesForPeriod(startDate, endDate,baseCurrency);
    }

    //Format exchange-rates/history/2023-01-01/2023-06-30/USD/EUR
    //Historical exchange rates between two currencies for a defined period
    @GetMapping("/historical/{startDate}/{endDate}/{baseCurrency}/{targetCurrency}")
    public String getHistoricalRatesBetweenCurrencies(
            @PathVariable String startDate,
            @PathVariable String endDate,
            @PathVariable String baseCurrency,
            @PathVariable String targetCurrency) {
        return exchangeRateService.getHistoricalRatesForCurrencies(startDate, endDate, baseCurrency, targetCurrency);
    }


    // Endpoint to get historical rates for two currencies from a specific date until today
    @GetMapping("/historical/{startDate}/{baseCurrency}/{targetCurrency}")
    public String getHistoricalRatesBetweenCurrenciesFromToNow(
            @PathVariable String startDate,
            @PathVariable String baseCurrency,
            @PathVariable String targetCurrency) {
        return exchangeRateService.getHistoricalRatesFromToNow(startDate, baseCurrency, targetCurrency);
    }


    // Endpoint to convert currency from one to another in the lastest rates
    @GetMapping("/convert")
    public Double convertCurrency(
            @RequestParam double amount,
            @RequestParam String from,
            @RequestParam String to) {
        return exchangeRateService.convertCurrency(amount, from, to);
    }
}