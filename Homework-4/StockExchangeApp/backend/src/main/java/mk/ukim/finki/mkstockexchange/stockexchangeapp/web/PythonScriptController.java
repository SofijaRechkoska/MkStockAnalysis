package mk.ukim.finki.mkstockexchange.stockexchangeapp.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/python")
public class PythonScriptController {

    @GetMapping("/run-script")
    public ResponseEntity<String> fetchPythonScriptOutput() {
        String pythonServiceUrl = "http://127.0.0.1:5002/filter3";

        try {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.getForEntity(pythonServiceUrl, String.class);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching data from Python service: " + e.getMessage());
        }
    }
}
