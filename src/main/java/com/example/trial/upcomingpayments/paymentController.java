package com.example.trial.upcomingpayments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class paymentController {
    @FXML private TableView<payment> paymentTable;
    @FXML private TableColumn<payment, String> nameColumn;
    @FXML private TableColumn<payment, Number> amountColumn;
    @FXML private TableColumn<payment, LocalDate> dateColumn;
    @FXML private TableColumn<payment, Boolean> paidColumn;

    @FXML private TextField nameField;
    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;

    private final ObservableList<payment> payments = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());
        paidColumn.setCellValueFactory(cellData -> cellData.getValue().paidProperty());

        paymentTable.setItems(payments);
    }

    @FXML
    private void handleAddPayment() {
        try {
            String name = nameField.getText();
            double amount = Double.parseDouble(amountField.getText());
            LocalDate date = datePicker.getValue();

            if (name.isEmpty() || date == null) {
                showAlert("Error", "Please fill all fields");
                return;
            }

            payments.add(new payment(name, amount, date));
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid amount");
        }
    }

    @FXML
    private void handleDeletePayment() {
        int selectedIndex = paymentTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            paymentTable.getItems().remove(selectedIndex);
        } else {
            showAlert("No Selection", "Please select a payment to delete");
        }
    }

    private void clearFields() {
        nameField.clear();
        amountField.clear();
        datePicker.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
