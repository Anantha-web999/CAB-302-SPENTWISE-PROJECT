package com.example.trial;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class SpendingAdvisorController {

    @FXML
    private TextArea userInput;

    @FXML
    private TextArea aiResponse;

    @FXML
    private TextField promptInput;

    @FXML
    private TextArea responseArea;


    @FXML
    private void clearChat() {
        userInput.clear();
        aiResponse.clear();
    }

    @FXML
    private void handleGetAdvice() {
        String prompt = userInput.getText();
        if (prompt == null || prompt.trim().isEmpty()) {
            aiResponse.setText("Please enter a spending concern.");
            return;
        }

        aiResponse.setText("Thinking...");

        new Thread(() -> {
            try {
                String result = getAdviceFromOllama(prompt);
                Platform.runLater(() -> aiResponse.setText(result));
            } catch (IOException e) {
                Platform.runLater(() -> aiResponse.setText("Failed to contact AI service: " + e.getMessage()));
            } catch (Exception e) {
                Platform.runLater(() -> aiResponse.setText("Error processing response: " + e.getMessage()));
            }
        }).start();
    }

    public void getAdvice(ActionEvent event) {
        String prompt = promptInput.getText();
        if (prompt.isEmpty()) {
            responseArea.setText("Please enter your question.");
            return;
        }

        responseArea.setText("Thinking...");

        new Thread(() -> {
            StringBuilder responseBuilder = new StringBuilder();
            try {
                URL url = new URL("http://localhost:11434/api/generate");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                String requestBody = String.format(
                        "{\"model\":\"llama3\",\"prompt\":\"%s\",\"stream\":true}",
                        prompt.replace("\"", "\\\"")
                );

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                    os.write(input);
                }

                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (!line.trim().isEmpty()) {
                            JSONObject json = new JSONObject(line);
                            if (json.has("response")) {
                                responseBuilder.append(json.getString("response"));
                                String partialResponse = responseBuilder.toString();
                                Platform.runLater(() -> responseArea.setText(partialResponse));
                            }
                        }
                    }
                }

            } catch (IOException e) {
                Platform.runLater(() -> responseArea.setText("Failed to contact AI service: " + e.getMessage()));
            } catch (Exception e) {
                Platform.runLater(() -> responseArea.setText("Error processing AI response: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hellofx/homepage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1000, 600));
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
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JSONObject json = new JSONObject(line);
                    if (json.has("response")) {
                        response.append(json.getString("response"));
                    }
                }
            }
            return response.toString();
        }
    }
}
