package mobex.ExchangeRate;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate;

    public ExchangeRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getLatestRates(String baseCurrency) {
        String url = "https://api.frankfurter.app/latest?base=" + baseCurrency;
        return restTemplate.getForObject(url, String.class);
    }


    public String getRatesForDate(String date, String baseCurrency) {
        String url = "https://api.frankfurter.app/" + date + "?base=" + baseCurrency;
        return restTemplate.getForObject(url, String.class);
    }


    public String getAllRatesForPeriod(String baseCurrency, String startDate, String endDate) {
        String url = "https://api.frankfurter.app/" + startDate + ".." + endDate + "?base=" + baseCurrency;
        return restTemplate.getForObject(url, String.class);
    }

    public String getHistoricalRatesForCurrencies(String startDate, String endDate, String baseCurrency, String targetCurrency) {
        String url = "https://api.frankfurter.app/" + startDate + ".." + endDate + "?from=" + baseCurrency + "&to=" + targetCurrency;
        return restTemplate.getForObject(url, String.class);
    }

    public String getHistoricalRatesFromToNow(String startDate, String baseCurrency, String targetCurrency) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.now();
        String url = "https://api.frankfurter.app/" + start + ".." + end + "?from=" + baseCurrency + "&to=" + targetCurrency;
        return restTemplate.getForObject(url, String.class);
    }

    //Convert currency from one to another
    public Double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.frankfurter.app/latest")
                .queryParam("amount", amount)
                .queryParam("from", fromCurrency)
                .queryParam("to", toCurrency)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        return rates.get(toCurrency);
    }
}