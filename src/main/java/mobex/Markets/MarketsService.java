package mobex.Markets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MarketsService {

    private final RestTemplate restTemplate;

    @Value("${rapidapi.key}")
    private String apiKey; // API key din application.properties

    private static final String STOCKS_DIRECTORY = "stocks_directory";
    private static final long ONE_WEEK = 604800000; // 1 săptămână în milisecunde
    // private static final long ONE_WEEK = 360000; // 1 oră în milisecunde

    @Autowired
    public MarketsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        createStocksDirectory(); // Asigură-te că directorul există la inițializare
    }

     // Creează directorul stocks_directory dacă nu există
    private void createStocksDirectory() {
        File directory = new File(STOCKS_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    // ***********************
    //  WORLD INDICES
    // ************************

    public String fetchWorldIndices() throws IOException {
        // Verifică dacă fișierul este actualizat
        if (isFileUpToDate()) {
            // Citește și returnează conținutul fișierului
            return new String(Files.readAllBytes(Paths.get("world_indices.json")));
        }

        // URL-ul API-ului
        String url = "https://yahoo-finance166.p.rapidapi.com/api/market/get-quote?symbols=%5EGSPC%2C%5EDJI%2C%5EIXIC%2C%5ENYA%2C%5EXAX%2C%5EBUK100P%2C%5ERUT%2C%5EVIX%2C%5EFTSE%2C%5EGDAXI%2C%5EFCHI%2C%5ESTOXX50E%2C%5EN100%2C%5EBFX%2CMOEX.ME%2CN225%2C%5EHSI%2C00001.SS%2C99001.SZ%2C%5ESTI%2C%5EAXJO%2C%5EAORD%2C%5BBSESN%2C%5EJKSE%2C%5EKLSE%2C%5ENZ50%2C%5EKS11%2C%5ETWII%2C%5EGSPTSE%2C%5EBVSP%2C%5EMXX%2C%5EIPSA%2C%5EMERV%2C%5ETA125.TA%2C%5ECASE30%2C%5EJN0U.JO%2CDX-Y.NYB%2C%5E125904-USD-STRD%2C%5EXDB%2C%5EXDE%2C000001.SS%2C%5EN225%2C%5EXDN%2C%5EXDA";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey); 
        headers.set("x-rapidapi-host", "yahoo-finance166.p.rapidapi.com");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Scrierea răspunsului în fișier
        writeResponseToFile(response.getBody());

        return response.getBody();
    }

    private boolean isFileUpToDate() {
        Path path = Paths.get("world_indices.json");
        try {
            return Files.exists(path) && Files.getLastModifiedTime(path).toMillis() > System.currentTimeMillis() - 604800000; // 1 săptămână
        } catch (IOException e) {            // În cazul în care apare o eroare la citirea fișierului, considerăm că fișierul nu este actualizat
            return false;
        }
    }

    private void writeResponseToFile(String response) throws IOException {
        try (FileWriter fileWriter = new FileWriter("world_indices.json")) {
            fileWriter.write(response);
        }
    }

    // **********************************
    //  STOCKS SIMBOL REQUESTS
    // **********************************

    // Obține datele pentru un simbol de stock
    public String fetchStockChartData(String symbol, String range, String interval) throws IOException {
        String filePath = STOCKS_DIRECTORY + "/" + symbol + ".json";
    
        // Verifică dacă fișierul există și este actualizat
        if (isFileUpToDate(filePath)) {
            return readFromFile(filePath); // Citește din fișier
        }
    
        // Dacă fișierul nu este actualizat, face request către API
        String url = "https://yahoo-finance166.p.rapidapi.com/api/stock/get-chart?region=US&range=" + range + "&symbol=" + symbol + "&interval=" + interval;
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", "yahoo-finance166.p.rapidapi.com");
    
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    
        // Scrie datele în fișier
        writeToFile(filePath, response.getBody());
    
        return response.getBody();
    }

    // Verifică dacă fișierul este actualizat
    private boolean isFileUpToDate(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.exists(path) && Files.getLastModifiedTime(path).toMillis() > System.currentTimeMillis() - ONE_WEEK;
        } catch (IOException e) {
            return false; // Dacă apare o eroare, consideră că fișierul nu este actualizat
        }
    }

    // Citește datele din fișier
    private String readFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return new String(Files.readAllBytes(path));
    }

    // Scrie datele în fișier
    private void writeToFile(String filePath, String data) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(data);
        }
    }

}
           


















































// CODUL DE MAI JOS DOAR CITESTE DIN FISIERUL world_indices.json SI IL RETURNEAZA

// package mobex.Markets;

// import org.springframework.stereotype.Service;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// @Service
// public class MarketsService {

//     // Metoda pentru a obține datele din fișierul world_indices.json
//     public String getWorldIndicesFromFile() throws IOException {
//         // Citește conținutul fișierului world_indices.json
//         Path path = Paths.get("world_indices.json");
//         if (!Files.exists(path)) {
//             throw new IOException("Fișierul world_indices.json nu a fost găsit.");
//         }
//         return new String(Files.readAllBytes(path));
//     }
// }