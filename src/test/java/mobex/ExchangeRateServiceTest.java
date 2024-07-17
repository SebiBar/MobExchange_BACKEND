package mobex;

import mobex.ExchangeRate.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ExchangeRateServiceTest {

    private final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    private final ExchangeRateService exchangeRateService = new ExchangeRateService(restTemplate);

    @Test
    public void testConvertCurrency() {
        // Mocking external API response
        String fromCurrency = "GBP";
        String toCurrency = "USD";
        double amount = 10.0;

        String url = "https://api.frankfurter.app/latest?amount=10.0&from=GBP&to=USD";

        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 13.0);
        mockResponse.put("rates", rates);

        when(restTemplate.getForObject(url, Map.class)).thenReturn(mockResponse);

        // Testing currency conversion
        Double convertedAmount = exchangeRateService.convertCurrency(amount, fromCurrency, toCurrency);
        assertEquals(13.0, convertedAmount);
    }

    @Test
    public void testConvertCurrencyWithDifferentRate() {
        // Mocking external API response with different rate
        String fromCurrency = "EUR";
        String toCurrency = "JPY";
        double amount = 100.0;

        String url = "https://api.frankfurter.app/latest?amount=100.0&from=EUR&to=JPY";

        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Double> rates = new HashMap<>();
        rates.put("JPY", 130.0);
        mockResponse.put("rates", rates);

        when(restTemplate.getForObject(url, Map.class)).thenReturn(mockResponse);

        // Testing currency conversion
        Double convertedAmount = exchangeRateService.convertCurrency(amount, fromCurrency, toCurrency);
        assertEquals(130.0, convertedAmount);
    }

    @Test
    public void testConvertCurrencyWithZeroAmount() {
        // Mocking external API response with zero amount
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        double amount = 0.0;

        String url = "https://api.frankfurter.app/latest?amount=0.0&from=USD&to=EUR";

        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.0);
        mockResponse.put("rates", rates);

        when(restTemplate.getForObject(url, Map.class)).thenReturn(mockResponse);

        // Testing currency conversion
        Double convertedAmount = exchangeRateService.convertCurrency(amount, fromCurrency, toCurrency);
        assertEquals(0.0, convertedAmount);
    }
}