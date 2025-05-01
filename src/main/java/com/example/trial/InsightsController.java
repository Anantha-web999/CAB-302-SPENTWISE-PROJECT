//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.trial;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class InsightsController {
    @FXML
    private PieChart categoryPieChart;
    @FXML
    private TableView<CategoryData> categoryTable;
    @FXML
    private TableColumn<CategoryData, String> colCategory;
    @FXML
    private TableColumn<CategoryData, Double> colAmount;
    @FXML
    private Label insightTip;
    @FXML
    private Label avgDailyLabel;
    @FXML
    private Label avgWeeklyLabel;
    @FXML
    private Label avgMonthlyLabel;
    @FXML
    private TableColumn<CategoryData, String> colPercentage;

    public InsightsController() {
    }

    @FXML
    private void goToCompareExpenses(ActionEvent event) {
        try {
            Parent compareRoot = (Parent)FXMLLoader.load(this.getClass().getResource("/com/example/trial/CompareExpenses.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(compareRoot, (double)800.0F, (double)600.0F));
            stage.setTitle("Compare Monthly Expenses");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize() {
        this.insightTip.setText("\ud83d\udca1 Insight: Your weekly spending is 5% lower than last month!");
        this.avgDailyLabel.setText("Daily Avg: $60");
        this.avgWeeklyLabel.setText("Weekly Avg: $367");
        this.avgMonthlyLabel.setText("Monthly Avg: $1340");
        this.colCategory.setCellValueFactory((data) -> ((CategoryData)data.getValue()).categoryProperty());
        this.colAmount.setCellValueFactory((data) -> ((CategoryData)data.getValue()).amountProperty().asObject());
        this.colPercentage.setCellValueFactory((data) -> ((CategoryData)data.getValue()).percentageProperty());
        this.loadData();
    }

    @FXML
    private void goToTrends(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/com/example/trial/Trends.fxml"));
            Parent root = (Parent)loader.load();
            TrendsController controller = (TrendsController)loader.getController();
            controller.setPreviousScene(((Node)event.getSource()).getScene());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, (double)800.0F, (double)600.0F));
            stage.setTitle("Weekly / Monthly Trends");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void loadData() {
        ObservableList<CategoryData> data = FXCollections.observableArrayList(new CategoryData[]{new CategoryData("Food", (double)4200.0F, "30%"), new CategoryData("Transport", (double)1800.0F, "13%"), new CategoryData("Shopping", (double)3500.0F, "25%"), new CategoryData("Entertainment", (double)1200.0F, "9%"), new CategoryData("Others", (double)4300.0F, "23%")});
        this.categoryTable.setItems(data);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for(CategoryData item : data) {
            pieChartData.add(new PieChart.Data(item.getCategory(), item.getAmount()));
        }

        this.categoryPieChart.setData(pieChartData);
    }

    public void goToHomePage(ActionEvent event) throws IOException {
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/com/example/hellofx/homepage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(homeRoot));

        stage.show();
    }
}



