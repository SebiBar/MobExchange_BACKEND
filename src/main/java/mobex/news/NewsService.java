package mobex.news;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import mobex.exceptions.CustomException; // Importă clasa CustomException

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


// imports for files reading and writing
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Service
public class NewsService {

    @Value("${news-api.apiKey}")
    private String apiKey;
    private RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String STOCKS_NEWS_URL = "https://newsapi.org/v2/everything?q=stocks&apiKey=%s";
    private static final String CRYPTO_NEWS_URL = "https://newsapi.org/v2/everything?q=cryptocurrency+bitcoin+ethereum&apiKey=%s";
    private static final String FOREX_NEWS_URL = "https://newsapi.org/v2/everything?q=currency+forex&apiKey=%s";
    private static final String REAL_ESTATE_NEWS_URL = "https://newsapi.org/v2/everything?q=real+estate&apiKey=%s";
    private static final String PRECIOUS_METALS_NEWS_URL = "https://newsapi.org/v2/everything?q=precious+metals+gold+silver&apiKey=%s";
    private static final String BUSINESS_NEWS_URL = "https://newsapi.org/v2/everything?q=business&sortBy=publishedAt&pageSize=100&apiKey=%s";

    private static final String JSON_FILE_PATH = "stocks_news.json"; // Calea fișierului JSON


    public NewsService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // public List<Map<String, Object>> getStocksNews() {
    //     String url = String.format(STOCKS_NEWS_URL, apiKey);
    //     return fetchNews(url);
    // }

    private void saveArticlesToFile(List<Map<String, Object>> articles) {
        try {
            objectMapper.writeValue(new File(JSON_FILE_PATH), articles); // Scrie articolele în fișier
        } catch (IOException e) {
            throw new CustomException("Error saving articles to file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Map<String, Object>> readArticlesFromFile() {
        try {
            File file = new File(JSON_FILE_PATH);
            if (file.exists() && file.length() > 0) {
                return objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {}); // Citește articolele din fișier
            }
        } catch (IOException e) {
            throw new CustomException("Error reading articles from file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null; // Returnăm null dacă fișierul nu există sau este gol
    }

    public List<Map<String, Object>> getStocksNews() {
        // Încercăm să citim datele din fișier
        List<Map<String, Object>> articles = readArticlesFromFile();
        if (articles != null && !articles.isEmpty()) {
            System.out.println("Citirea articolelor din fisier JSON."); // Logare
            return articles; // Returnăm articolele din fișier
        }

        // Dacă fișierul nu există sau este gol, facem un request la API
        String url = String.format(STOCKS_NEWS_URL, apiKey);
        System.out.println("Facem request la API pentru a obtine articolele."); // Logare
        articles = fetchNews(url);
        
        // Salvăm articolele în fișier
        saveArticlesToFile(articles);
        
        return articles;
    }

    public List<Map<String, Object>> getCryptoNews() {
        String url = String.format(CRYPTO_NEWS_URL, apiKey);
        return fetchNews(url);
    }

    public List<Map<String, Object>> getForexNews() {
        String url = String.format(FOREX_NEWS_URL, apiKey);
        return fetchNews(url);
    }

  
    public List<Map<String, Object>> getRealEstateNews() {
        String url = String.format(REAL_ESTATE_NEWS_URL, apiKey);
        return fetchNews(url);
    }

    public List<Map<String, Object>> getPreciousMetalsNews() {
        String url = String.format(PRECIOUS_METALS_NEWS_URL, apiKey);
        return fetchNews(url);
    }

    public List<Map<String, Object>> getBusinessNews() {
        String url = String.format(BUSINESS_NEWS_URL, apiKey);
        return fetchNews(url);
    }

    private List<Map<String, Object>> fetchNews(String url) {
    try {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        // Verificăm statusul răspunsului
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            if (body != null) {
                Object articlesObject = body.get("articles");
                if (articlesObject instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> articles = (List<Map<String, Object>>) articlesObject;
                    return articles; // Returnăm lista de articole
                } else {
                    throw new CustomException("No articles found in the response.", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new CustomException("Response body is null.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            // Gestionăm codurile de stare diferite
            if (response.getStatusCode() == HttpStatus.OK) {
                // Cod pentru gestionarea răspunsului OK
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new CustomException("Bad Request: The request was unacceptable.", HttpStatus.BAD_REQUEST);
            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new CustomException("Unauthorized: API key missing or incorrect.", HttpStatus.UNAUTHORIZED);
            } else if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new CustomException("Too Many Requests: Rate limit exceeded.", HttpStatus.TOO_MANY_REQUESTS);
            } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new CustomException("Server Error: Something went wrong on our side.", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                throw new CustomException("Unexpected error: " + response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    } catch (HttpClientErrorException e) {
        // Capturăm erorile 4xx
        throw new CustomException("Client error: " + e.getMessage(), (HttpStatus) e.getStatusCode());
    } catch (HttpServerErrorException e) {
        // Capturăm erorile 5xx
        throw new CustomException("Server error: " + e.getMessage(), (HttpStatus) e.getStatusCode());
    } catch (Exception e) {
        System.out.println("Error fetching news: " + e.getMessage());
        throw new CustomException("Error fetching news: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Returnăm o listă goală în cazul în care nu s-a returnat nimic anterior
    return new ArrayList<>(); // Asigurăm că metoda returnează o listă
}
}