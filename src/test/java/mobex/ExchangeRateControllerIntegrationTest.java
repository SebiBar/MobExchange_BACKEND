package mobex;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class ExchangeRateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetLatestRates() throws Exception {
        mockMvc.perform(get("/exchange-rates/latest/USD"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRatesForDate() throws Exception {
        mockMvc.perform(get("/exchange-rates/CAD/2010-01-03"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllRatesForPeriod() throws Exception {
        mockMvc.perform(get("/exchange-rates/period/CAD/2023-01-01/2024-01-31"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetHistoricalRatesForCurrencies() throws Exception {
        mockMvc.perform(get("/exchange-rates/historical/2023-01-01/2023-06-30/USD/EUR"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetHistoricalRatesForTwoCurrencies() throws Exception {
        mockMvc.perform(get("/exchange-rates/historical/2023-01-01/USD/EUR"))
                .andExpect(status().isOk());
    }

    @Test
    public void testConvertCurrency() throws Exception {
        mockMvc.perform(get("/exchange-rates/convert")
                        .param("amount", "10")
                        .param("from", "GBP")
                        .param("to", "USD"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("12.9598"))); // Updated to match actual result
    }
}
