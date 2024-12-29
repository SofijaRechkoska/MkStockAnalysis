package mk.ukim.finki.mkstockexchange.stockexchangeapp.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@RestController
@RequestMapping("/python")
public class PythonScriptController {

    @GetMapping("/run-script")
    public String runPythonScript() {
        String pythonExecutablePath = "python";

        try {
            File scriptFile = new ClassPathResource("scripts/python/demo/Filter3.py").getFile();
            String pythonScriptPath = scriptFile.getAbsolutePath();

            ProcessBuilder processBuilder = new ProcessBuilder(pythonExecutablePath, pythonScriptPath);

            Map<String, String> environment = processBuilder.environment();
            environment.put("PYTHONPATH", scriptFile.getParentFile().getAbsolutePath());

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            output.append("Process exited with code: ").append(exitCode);

            return output.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error running Python script: " + e.getMessage();
        }
    }
}
