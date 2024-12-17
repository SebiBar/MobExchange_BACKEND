// package mobex.Markets;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import java.io.FileWriter;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// @Service
// public class MarketsService {

//     private final RestTemplate restTemplate;

//     @Value("${rapidapi.key}")
//     private String apiKey; // API key din application.properties

//     @Autowired
//     public MarketsService(RestTemplate restTemplate) {
//         this.restTemplate = restTemplate;
//     }

//     public String fetchWorldIndices() throws IOException {
//         // Verifică dacă fișierul este actualizat
//         if (isFileUpToDate()) {
//             // Citește și returnează conținutul fișierului
//             return new String(Files.readAllBytes(Paths.get("world_indices.json")));
//         }

//         // URL-ul API-ului
//         String url = "https://yahoo-finance166.p.rapidapi.com/api/market/get-quote?symbols=%5EGSPC%2C%5EDJI%2C%5EIXIC%2C%5ENYA%2C%5EXAX%2C%5EBUK100P%2C%5ERUT%2C%5EVIX%2C%5EFTSE%2C%5EGDAXI%2C%5EFCHI%2C%5ESTOXX50E%2C%5EN100%2C%5EBFX%2CMOEX.ME%2CN225%2C%5EHSI%2C00001.SS%2C99001.SZ%2C%5ESTI%2C%5EAXJO%2C%5EAORD%2C%5BBSESN%2C%5EJKSE%2C%5EKLSE%2C%5ENZ50%2C%5EKS11%2C%5ETWII%2C%5EGSPTSE%2C%5EBVSP%2C%5EMXX%2C%5EIPSA%2C%5EMERV%2C%5ETA125.TA%2C%5ECASE30%2C%5EJN0U.JO%2CDX-Y.NYB%2C%5E125904-USD-STRD%2C%5EXDB%2C%5EXDE%2C000001.SS%2C%5EN225%2C%5EXDN%2C%5EXDA";

//         HttpHeaders headers = new HttpHeaders();
//         headers.set("x-rapidapi-key", apiKey); 
//         headers.set("x-rapidapi-host", "yahoo-finance166.p.rapidapi.com");

//         HttpEntity<String> entity = new HttpEntity<>(headers);
//         ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

//         // Scrierea răspunsului în fișier
//         writeResponseToFile(response.getBody());

//         return response.getBody();
//     }

//     private boolean isFileUpToDate() {
//         Path path = Paths.get("world_indices.json");
//         try {
//             return Files.exists(path) && Files.getLastModifiedTime(path).toMillis() > System.currentTimeMillis() - 3600000; // 1 oră
//         } catch (IOException e) {            // În cazul în care apare o eroare la citirea fișierului, considerăm că fișierul nu este actualizat
//             return false;
//         }
//     }

//     private void writeResponseToFile(String response) throws IOException {
//         try (FileWriter fileWriter = new FileWriter("world_indices.json")) {
//             fileWriter.write(response);
//         }
//     }
// }
           

package mobex.Markets;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MarketsService {

    // Metoda pentru a obține datele din fișierul world_indices.json
    public String getWorldIndicesFromFile() throws IOException {
        // Citește conținutul fișierului world_indices.json
        Path path = Paths.get("world_indices.json");
        if (!Files.exists(path)) {
            throw new IOException("Fișierul world_indices.json nu a fost găsit.");
        }
        return new String(Files.readAllBytes(path));
    }
}