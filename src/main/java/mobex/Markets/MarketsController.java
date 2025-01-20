package mobex.Markets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException;

@RestController
@RequestMapping("/markets")
public class MarketsController {

    private final MarketsService marketsService;

    @Autowired
    public MarketsController(MarketsService marketsService) {
        this.marketsService = marketsService;
    }

    @Operation(summary = "Get World Indices",
               description = "Retrieves the latest data for various world indices from the local file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved world indices data."),
            @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/world-indices")
    public ResponseEntity<String> getWorldIndices() {
        try {
            String response = marketsService.fetchWorldIndices();
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error writing to file: " + e.getMessage());
        }
    }



    @Operation(summary = "Get Futures",
               description = "Retrieves the latest data for various futures from the local file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved futures data."),
            @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/commodities")
    public ResponseEntity<String> getFutures() {
        try {
            String response = marketsService.fetchFutures();
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error writing to file: " + e.getMessage());
        }
    }


    // **********************************
    //  STOCKS SIMBOL REQUESTS
    // **********************************
    @Operation(summary = "Get Stock Chart Data",
           description = "Retrieves the latest chart data for a specific stock symbol.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stock chart data."),
            @ApiResponse(responseCode = "401", description = "Invalid API key."),
            @ApiResponse(responseCode = "403", description = "You are not subscribed to this API."),
            @ApiResponse(responseCode = "429", description = "You have exceeded the MONTHLY quota for Requests on your current plan, BASIC. Upgrade your plan for more requests."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/stock-chart")
    public ResponseEntity<String> getStockChartData(
            @RequestParam String symbol,
            @RequestParam String range,
            @RequestParam String interval
        ) {
        try {
            String response = marketsService.fetchStockChartData(symbol, range, interval);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error fetching stock chart data: " + e.getMessage());
        }
    }
}