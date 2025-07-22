

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
    //private static final long ONE_MONTH = 2592000000L; // 1 lună în milisecunde
    private static final long ONE_DAY = 86400000L; // 1 zi în milisecunde  
    //private static final long ONE_MINUTE = 55000; // 55 secunde in milisecunde 
    // private static final long ONE_WEEK = 604800000; // Milisecunde într-o săptămână
    private static final long ONE_MONTH = 2592000000L; // Milisecunde într-o lună de 30 de zile
    //private static final long UPDATE_INTERVAL = 10000; // 10 secunde in milisecunde

   

    @Autowired
    public MarketsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        createStocksDirectory(); // Asigură-te că directorul există la inițializare
    }

    public String fetchAutocompleteData(String url) throws IOException {  
        HttpHeaders headers = new HttpHeaders();  
        headers.set("x-rapidapi-key", apiKey);  
        headers.set("x-rapidapi-host", "yahoo-finance166.p.rapidapi.com");  

        HttpEntity<String> entity = new HttpEntity<>(headers);  

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);  

        return responseEntity.getBody();  
    } 

    
    public String fetchDataFor1Asset(String url) throws IOException {  
        HttpHeaders headers = new HttpHeaders();  
        headers.set("x-rapidapi-key", apiKey);   
        headers.set("x-rapidapi-host", "yahoo-finance166.p.rapidapi.com");  

        HttpEntity<String> entity = new HttpEntity<>(headers);  

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);  

        return responseEntity.getBody();  
    } 

    
    public String fetchData(String fileName, String url) throws IOException {  
        // Verifică dacă fișierul exista si daca este actualizat  
        if (isFileUpToDate(fileName)) {  
            // Daca continutul este actualizat, citește și returnează conținutul fișierului  
            return new String(Files.readAllBytes(Paths.get(fileName)));  
        }  
    
        // Dacă fișierul nu există sau nu este actualizat, faceți un request la API
        // și scrieți răspunsul în fișier
        HttpHeaders headers = new HttpHeaders();  
        headers.set("x-rapidapi-key", apiKey);   
        headers.set("x-rapidapi-host", "yahoo-finance166.p.rapidapi.com");  
    
        HttpEntity<String> entity = new HttpEntity<>(headers);  
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);  
    
        // Scrierea răspunsului în fișier  
        writeResponseToFile(response.getBody(), fileName);  
    
        return response.getBody();  
    }  
    
    // Metoda pentru a verifica dacă fișierul exista si daca a fost actualizat în ultimele 24 de ore
    private boolean isFileUpToDate(String fileName) {  
        Path path = Paths.get(fileName);  
        try {  
            return Files.exists(path) && Files.getLastModifiedTime(path).toMillis() > System.currentTimeMillis() - ONE_MONTH; // la o durata de 1 luna se actualizeaza datele. 
        } catch (IOException e) {  
            // În cazul în care apare o eroare la citirea fișierului, considerăm că fișierul nu este actualizat  
            return false;  
        }  
    }  
    
    private void writeResponseToFile(String response, String fileName) throws IOException {  
        try (FileWriter fileWriter = new FileWriter(fileName)) {  
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

        // Creează subdirectoare pentru fiecare interval de timp
        String[] subdirectories = {"1D", "5D", "1M", "6M", "YTD", "1Y", "5Y", "ALL"};
        for (String subdirectory : subdirectories) {
            File subdirectoryFile = new File(STOCKS_DIRECTORY + "/" + subdirectory);
            if (!subdirectoryFile.exists()) {
                subdirectoryFile.mkdir();
            }
        }
    }

    private String readOrCreateFile(String symbol, String range, String interval) {  
        // Determină subdirectorul în funcție de intervalul de timp (range)  
        String subdirectory = getSubdirectory(range);  

        // Construiește numele fișierului în format simbol_subdirector.json  
        String filename = symbol + "_" + subdirectory + ".json";  

        // Construiește calea completă către fișier în directorul specificat  
        String filepath = STOCKS_DIRECTORY + "/" + subdirectory + "/" + filename;   
        
        // Verifică dacă fișierul este actualizat în ultimele 24h  
        if (isFileUpToDate(filepath)) {  
            try { 
                // Dacă fișierul este actualizat, citește și returnează conținutul său  
                return new String(Files.readAllBytes(Paths.get(filepath)));  
            } catch (IOException e) {  
                // Dacă citirea eșuează, continuă să preia date de la API  
            }  
        }  

        // Dacă fișierul nu există sau nu este actualizat în ultimele 24h, preia datele noi și scrie în fișier  
        return fetchStockChartDataFromAPI(symbol, range, interval);  
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



    // This method gets news for symbols from api.
    
    public String fetchNewsForSymbols(String symbols) throws IOException {  
        HttpHeaders headers = new HttpHeaders();  
        headers.set("x-rapidapi-key", apiKey);   
        headers.set("x-rapidapi-host", "yahoo-finance166.p.rapidapi.com");  

        HttpEntity<String> entity = new HttpEntity<>(headers);  

        String url = "https://yahoo-finance166.p.rapidapi.com/api/news/list-by-symbol?s=" + symbols + "&region=US&snippetCount=25";

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);  

        return responseEntity.getBody();  
    } 


}
