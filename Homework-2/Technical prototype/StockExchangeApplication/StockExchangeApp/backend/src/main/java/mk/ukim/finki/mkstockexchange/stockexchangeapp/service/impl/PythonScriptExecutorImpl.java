package mk.ukim.finki.mkstockexchange.stockexchangeapp.service.impl;

import mk.ukim.finki.mkstockexchange.stockexchangeapp.service.PythonScriptExecutor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class PythonScriptExecutorImpl implements PythonScriptExecutor {


    private final String pythonExecutable = "python";

    @Override
    public void executeScript(String scriptPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(pythonExecutable, scriptPath);
            Process process = pb.start();

            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = outputReader.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }


            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.out.println("Python script execution failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
