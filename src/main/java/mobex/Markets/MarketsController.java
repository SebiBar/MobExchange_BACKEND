package mobex.Markets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException; 
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/markets")
public class MarketsController {

    private final MarketsService marketsService;

    @Autowired
    public MarketsController(MarketsService marketsService) {
        this.marketsService = marketsService;
    }

    @Operation(summary = "Get World Indices",
               description = "Retrieves the latest data for various world indices such as S&P 500, Dow Jones, NASDAQ, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved world indices data."),
            @ApiResponse(responseCode = "401", description = "Invalid API key. Go to https://docs.rapidapi.com/docs/keys for more info."),
            @ApiResponse(responseCode = "403", description = "You are not subscribed to this API."),
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
}