package com.example.trial;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;


public class SpendingAdvisorController {

    @FXML
    private TextArea userInput;

    @FXML
    private TextArea aiResponse;

    @FXML
    private void handleGetAdvice() {
        String prompt = userInput.getText();
        if (prompt == null || prompt.trim().isEmpty()) {
            aiResponse.setText("Please enter a spending concern.");
            return;
        }

        try {
            String result = getAdviceFromOllama(prompt);
            aiResponse.setText(result);
        } catch (IOException e) {
            aiResponse.setText("Failed to contact AI service: " + e.getMessage());
        }
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trial/Insights.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Insights - SpentWise");
        stage.show();
    }


    private String getAdviceFromOllama(String prompt) throws IOException {
        URL url = new URL("http://localhost:11434/api/generate");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = String.format("{\"model\":\"llama3\",\"prompt\":\"%s\"}", prompt.replace("\"", "\\\""));

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            return response.toString();
        }
    }
}
