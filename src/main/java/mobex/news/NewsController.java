package mobex.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mobex.exceptions.CustomException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "Get stocks news", description = "Returns a list of stocks news.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Successfully retrieved stocks news."),
            @ApiResponse(responseCode = "400", description = "Bad Request. The request was unacceptable, often due to a missing or misconfigured parameter."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Your API key was missing from the request, or wasn't correct."),
            @ApiResponse(responseCode = "429", description = "Too Many Requests. You made too many requests within a window of time and have been rate limited. Back off for a while."),
            @ApiResponse(responseCode = "500", description = "Server Error. Something went wrong on NewsAPI server.")
    })
    @GetMapping("/stocks")
    public ResponseEntity<?> getStocksNews() {
        try {
            List<Map<String, Object>> news = newsService.getStocksNews();
            return ResponseEntity.ok(news);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @Operation(summary = "Get crypto news", description = "Returns a list of crypto news.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Successfully retrieved crypto news."),
            @ApiResponse(responseCode = "400", description = "Bad Request. The request was unacceptable, often due to a missing or misconfigured parameter."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Your API key was missing from the request, or wasn't correct."),
            @ApiResponse(responseCode = "429", description = "Too Many Requests. You made too many requests within a window of time and have been rate limited. Back off for a while."),
            @ApiResponse(responseCode = "500", description = "Server Error. Something went wrong on NewsAPI server.")
    })
    @GetMapping("/crypto")
    public ResponseEntity<?> getCryptoNews() {
        try {
            List<Map<String, Object>> news = newsService.getCryptoNews();
            return ResponseEntity.ok(news);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @Operation(summary = "Get forex news", description = "Returns a list of forex news.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Successfully retrieved forex news."),
            @ApiResponse(responseCode = "400", description = "Bad Request. The request was unacceptable, often due to a missing or misconfigured parameter."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Your API key was missing from the request, or wasn't correct."),
            @ApiResponse(responseCode = "429", description = "Too Many Requests. You made too many requests within a window of time and have been rate limited. Back off for a while."),
            @ApiResponse(responseCode = "500", description = "Server Error. Something went wrong on NewsAPI server.")
    })
    @GetMapping("/forex")
    public ResponseEntity<?> getForexNews() {
        try {
            List<Map<String, Object>> news = newsService.getForexNews();
            return ResponseEntity.ok(news);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @Operation(summary = "Get real-estate news", description = "Returns a list of real-estate news.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Successfully retrieved real-estate news."),
            @ApiResponse(responseCode = "400", description = "Bad Request. The request was unacceptable, often due to a missing or misconfigured parameter."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Your API key was missing from the request, or wasn't correct."),
            @ApiResponse(responseCode = "429", description = "Too Many Requests. You made too many requests within a window of time and have been rate limited. Back off for a while."),
            @ApiResponse(responseCode = "500", description = "Server Error. Something went wrong on NewsAPI server.")
    })
    @GetMapping("/real-estate")
    public ResponseEntity<?> getRealEstateNews() {
        try {
            List<Map<String, Object>> news = newsService.getRealEstateNews();
            return ResponseEntity.ok(news);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @Operation(summary = "Get precious-metals news", description = "Returns a list of precious metals news.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Successfully retrieved precious metals news."),
            @ApiResponse(responseCode = "400", description = "Bad Request. The request was unacceptable, often due to a missing or misconfigured parameter."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Your API key was missing from the request, or wasn't correct."),
            @ApiResponse(responseCode = "429", description = "Too Many Requests. You made too many requests within a window of time and have been rate limited. Back off for a while."),
            @ApiResponse(responseCode = "500", description = "Server Error. Something went wrong on NewsAPI server.")
    })
    @GetMapping("/precious-metals")
    public ResponseEntity<?> getPreciousMetalsNews() {
        try {
            List<Map<String, Object>> news = newsService.getPreciousMetalsNews();
            return ResponseEntity.ok(news);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @Operation(summary = "Get business news", description = "Returns a list of business news.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK. Successfully retrieved business news."),
            @ApiResponse(responseCode = "400", description = "Bad Request. The request was unacceptable, often due to a missing or misconfigured parameter."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Your API key was missing from the request, or wasn't correct."),
            @ApiResponse(responseCode = "429", description = "Too Many Requests. You made too many requests within a window of time and have been rate limited. Back off for a while."),
            @ApiResponse(responseCode = "500", description = "Server Error. Something went wrong on NewsAPI server.")
    })
    @GetMapping("/business")
    public ResponseEntity<?> getBusinessNews() {
        try {
            List<Map<String, Object>> news = newsService.getBusinessNews();
            return ResponseEntity.ok(news);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
}
