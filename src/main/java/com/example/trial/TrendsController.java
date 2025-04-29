//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.trial;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class TrendsController {
    @FXML
    private ComboBox<String> timeRangeCombo;
    @FXML
    private LineChart<String, Number> trendChart;
    private Scene previousScene;

    public TrendsController() {
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    @FXML
    public void initialize() {
        this.timeRangeCombo.setItems(FXCollections.observableArrayList(new String[]{"Weekly", "Monthly"}));
        this.timeRangeCombo.getSelectionModel().selectFirst();
        this.loadTrendData();
    }

    @FXML
    public void loadTrendData() {
        this.trendChart.getData().clear();
        String selection = (String)this.timeRangeCombo.getValue();
        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName(selection + " Spending");
        if ("Weekly".equals(selection)) {
            series.getData().add(new XYChart.Data("Week 1", 2200));
            series.getData().add(new XYChart.Data("Week 2", 1800));
            series.getData().add(new XYChart.Data("Week 3", 2400));
            series.getData().add(new XYChart.Data("Week 4", 2100));
        } else {
            series.getData().add(new XYChart.Data("Jan", 8200));
            series.getData().add(new XYChart.Data("Feb", 7600));
            series.getData().add(new XYChart.Data("Mar", 9100));
            series.getData().add(new XYChart.Data("Apr", 8800));
        }

        this.trendChart.getData().add(series);
    }

    @FXML
    public void goBack(ActionEvent event) {
        if (this.previousScene != null) {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(this.previousScene);
            stage.setTitle("Insights");
            stage.show();
        }

    }
}
