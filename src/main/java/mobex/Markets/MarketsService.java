

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

    private static final String STOCKS_DIRECTORY = "historical_data_for_assets";
    private static final long ONE_MONTH = 2592000000L; // 1 lună în milisecunde
   

    @Autowired
    public MarketsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        createStocksDirectory(); // Asigură-te că directorul există la inițializare
    }

    // ***********************
    //  WORLD INDICES
    // ************************

    public String fetchWorldIndices() throws IOException {
        // Verifică dacă fișierul este actualizat
        // fisierul contine un array cu 40 de obiecte, iar fiecare obiect contine date generale despre un asset, NU CONTINE DATE ISTORICE necesare pentru a crea un grafic !!
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
            return Files.exists(path) && Files.getLastModifiedTime(path).toMillis() > System.currentTimeMillis() - ONE_MONTH; // 1 luna
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

    private void createStocksDirectory() {
        File directory = new File(STOCKS_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }

        // String[] subdirectories = {"1D", "5D", "1M", "3M", "6M", "1Y", "5Y", "ALL"};
        String[] subdirectories = {"1D", "5D", "1M", "6M", "YTD", "1Y", "5Y", "ALL"};
        for (String subdirectory : subdirectories) {
            File subdirectoryFile = new File(STOCKS_DIRECTORY + "/" + subdirectory);
            if (!subdirectoryFile.exists()) {
                subdirectoryFile.mkdir();
            }
        }
    }

    private String readOrCreateFile(String symbol, String range, String interval) {
        String subdirectory = getSubdirectory(range);
        String filename = symbol + "_" + subdirectory + ".json";
        String filepath = STOCKS_DIRECTORY + "/" + subdirectory + "/" + filename;

        File file = new File(filepath);
        if (file.exists() && file.isFile()) {
            try {
                return new String(Files.readAllBytes(Paths.get(filepath)));
            } catch (IOException e) {
                // Dacă fișierul este corupt, facem un request la API
                return fetchStockChartDataFromAPI(symbol, range, interval);
            }
        } else {
            // Dacă fișierul nu există, facem un request la API
            return fetchStockChartDataFromAPI(symbol, range, interval);
        }
    }

    private String fetchStockChartDataFromAPI(String symbol, String range, String interval) {
        // Codul pentru a face un request la API și a scrie datele în fișier
        String url = "https://yahoo-finance166.p.rapidapi.com/api/stock/get-chart?region=US&range=" + range + "&symbol=" + symbol + "&interval=" + interval;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", "yahoo-finance166.p.rapidapi.com");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String filepath = STOCKS_DIRECTORY + "/" + getSubdirectory(range) + "/" + symbol + "_" + getSubdirectory(range) + ".json";
        writeToFile(filepath, response.getBody());

        return response.getBody();
    }

    private void writeToFile(String filepath, String data) {
        try (FileWriter fileWriter = new FileWriter(filepath)) {
            fileWriter.write(data);
        } catch (IOException e) {
            // Gestionăm eroarea
        }
    }

    private String getSubdirectory(String range) {
        switch (range) {
            case "1d":
                return "1D";
            case "5d":
                return "5D";
            case "1mo":
                return "1M";
            case "6mo":
                return "6M";
            case "ytd":
                return "YTD";
            case "1y":
                return "1Y";
            case "5y":
                return "5Y";
            case "max":
                return "ALL";
            default:
                return "1D";
        }
    }

    public String fetchStockChartData(String symbol, String range, String interval) throws IOException {
        return readOrCreateFile(symbol, range, interval);
    }
}
