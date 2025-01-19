package mk.ukim.finki.mkstockexchange.stockexchangeapp.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class PythonScriptRunner {

    private final ResourceLoader resourceLoader;

    @Value("${python.script.path}")
    private String pythonScriptPath;

    public PythonScriptRunner(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String run() {
        try {
            Resource resource = resourceLoader.getResource(pythonScriptPath);
            String scriptAbsolutePath = resource.getFile().getAbsolutePath();

            System.out.println("Running Python script at path: " + scriptAbsolutePath);

            ProcessBuilder processBuilder = new ProcessBuilder("python", scriptAbsolutePath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();

            System.out.println("Python script output: \n" + output.toString());
            return "Python script executed successfully! Output: " + output.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error executing Python script.";
        }
    }
}
