//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.trial.insights_AI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;


public class CompareExpensesController {
    @FXML
    private ComboBox<String> month1Combo;
    @FXML
    private ComboBox<String> month2Combo;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private TableView<ComparisonData> comparisonTable;
    @FXML
    private TableColumn<ComparisonData, String> colCategory;
    @FXML
    private TableColumn<ComparisonData, Double> colMonth1;
    @FXML
    private TableColumn<ComparisonData, Double> colMonth2;
    @FXML
    private TableColumn<ComparisonData, String> colDifference;

    public CompareExpensesController() {
    }

    @FXML
    public void initialize() {
        this.month1Combo.setItems(FXCollections.observableArrayList(new String[]{"January", "February", "March","April", "May", "June",
                "July", "August", "September", "October", "November", "December"}));
        this.month2Combo.setItems(FXCollections.observableArrayList(new String[]{"January", "February", "March","April", "May", "June",
                "July", "August", "September", "October", "November", "December"}));
        this.month1Combo.getSelectionModel().selectFirst();
        this.month2Combo.getSelectionModel().select(1);
        this.colCategory.setCellValueFactory((data) -> ((ComparisonData)data.getValue()).categoryProperty());
        this.colMonth1.setCellValueFactory((data) -> ((ComparisonData)data.getValue()).month1ValueProperty().asObject());
        this.colMonth2.setCellValueFactory((data) -> ((ComparisonData)data.getValue()).month2ValueProperty().asObject());
        this.colDifference.setCellValueFactory((data) -> ((ComparisonData)data.getValue()).differenceProperty());
        this.loadComparisonData();
    }

    @FXML
    private void goBackToInsights(ActionEvent event) {
        try {
            Parent insightsRoot = (Parent)FXMLLoader.load(this.getClass().getResource("/com/example/trial/Insights.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(insightsRoot, (double)1000.0F, (double)600.0F));
            stage.setTitle("Insights");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void loadComparisonData() {
        ObservableList<ComparisonData> data = FXCollections.observableArrayList(new ComparisonData[]{new ComparisonData("Food", (double)4200.0F, (double)3600.0F), new ComparisonData("Transport", (double)1800.0F, (double)2300.0F), new ComparisonData("Shopping", (double)3500.0F, (double)3100.0F), new ComparisonData("Entertainment", (double)1200.0F, (double)1500.0F)});
        this.comparisonTable.setItems(data);
        this.barChart.getData().clear();
        XYChart.Series<String, Number> series1 = new XYChart.Series();
        series1.setName((String)this.month1Combo.getValue());
        XYChart.Series<String, Number> series2 = new XYChart.Series();
        series2.setName((String)this.month2Combo.getValue());

        for(ComparisonData d : data) {
            series1.getData().add(new XYChart.Data(d.getCategory(), d.getMonth1Value()));
            series2.getData().add(new XYChart.Data(d.getCategory(), d.getMonth2Value()));
        }

        this.barChart.getData().addAll(new XYChart.Series[]{series1, series2});
    }

    @FXML
    private void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Comparison Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("compare_expenses.csv");

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                // Write CSV header
                writer.println("Category,Month1,Month2,Difference");

                // Write table data
                for (ComparisonData data : comparisonTable.getItems()) {
                    writer.printf("%s,%.2f,%.2f,%s%n",
                            data.getCategory(),
                            data.getMonth1Value(),
                            data.getMonth2Value(),
                            data.getDifference());
                }

                // Success popup
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export");
                alert.setHeaderText(null);
                alert.setContentText("Comparison data exported successfully!");
                alert.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
