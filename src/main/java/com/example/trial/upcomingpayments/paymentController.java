package com.example.trial.upcomingpayments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller for the Upcoming Payments screen.
 * Handles UI interactions: adding, deleting payments, and navigation.
 */
public class paymentController {
    // UI components hooked up via FXML
    @FXML private TableView<payment> paymentTable;                // Table that displays all payments
    @FXML private TableColumn<payment, String> nameColumn;        // Table column for payment name
    @FXML private TableColumn<payment, Number> amountColumn;      // Table column for payment amount
    @FXML private TableColumn<payment, LocalDate> dateColumn;     // Table column for due date
    @FXML private TableColumn<payment, Boolean> paidColumn;       // Table column for paid status

    @FXML private TextField nameField;    // Input for the payment name
    @FXML private TextField amountField;  // Input for the payment amount
    @FXML private DatePicker datePicker;  // Input for the payment due date

    // List to store all the payments shown in the table
    private final ObservableList<payment> payments = FXCollections.observableArrayList();

    /**
     * Called automatically after the FXML is loaded.
     * Sets up the table columns and binds them to payment properties.
     */
    @FXML
    public void initialize() {
        // Bind each table column to the relevant property in the payment class
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());
        paidColumn.setCellValueFactory(cellData -> cellData.getValue().paidProperty());

        // Set the table's items to our payments list
        paymentTable.setItems(payments);
    }

    /**
     * Adds a new payment when the "Add" button is clicked.
     * Reads input fields, validates, creates a payment object, and adds it to the table.
     */
    @FXML
    private void handleAddPayment() {
        try {
            String name = nameField.getText();                     // Get payment name from input
            double amount = Double.parseDouble(amountField.getText()); // Parse payment amount from input
            LocalDate date = datePicker.getValue();                // Get due date from input

            // Simple validation: make sure all fields are filled
            if (name.isEmpty() || date == null) {
                showAlert("Error", "Please fill all fields");
                return;
            }

            // Add new payment to the list (and thus to the table)
            payments.add(new payment(name, amount, date));
            clearFields();  // Reset input fields for next entry
        } catch (NumberFormatException e) {
            // If amount isn't a valid number, show a warning
            showAlert("Invalid Input", "Please enter a valid amount");
        }
    }

    /**
     * Deletes the selected payment when the "Delete" button is clicked.
     * Removes it from the payments list and table.
     */
    @FXML
    private void handleDeletePayment() {
        int selectedIndex = paymentTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // If a payment is selected, remove it
            paymentTable.getItems().remove(selectedIndex);
        } else {
            // If nothing is selected, warn the user
            showAlert("No Selection", "Please select a payment to delete");
        }
    }

    /**
     * Clears all the input fields (name, amount, and date).
     * Called after adding a payment or when resetting the form.
     */
    private void clearFields() {
        nameField.clear();
        amountField.clear();
        datePicker.setValue(null);
    }

    /**
     * Helper method to show warning alerts to the user.
     * Used for input validation and error handling.
     *
     * @param title   The alert window title
     * @param content The message to display
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Handles navigation back to the homepage.
     * Loads the homepage FXML and replaces the current scene.
     *
     * @param event The event triggered by clicking "Back"
     * @throws IOException if the FXML file can't be loaded
     */
    public void handleGoBack(ActionEvent event) throws IOException {
        // Load the homepage scene from FXML
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/hellofx/homepage.fxml"));
        // Get the current window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Set the new scene (homepage)
        stage.setScene(new Scene(homeRoot));
        stage.show();
    }
}
