//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.trial;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CategoryData {
    private final StringProperty category;
    private final DoubleProperty amount;
    private final StringProperty percentage;

    public CategoryData(String category, double amount, String percentage) {
        this.category = new SimpleStringProperty(category);
        this.amount = new SimpleDoubleProperty(amount);
        this.percentage = new SimpleStringProperty(percentage);
    }

    public String getCategory() {
        return (String)this.category.get();
    }

    public double getAmount() {
        return this.amount.get();
    }

    public String getPercentage() {
        return (String)this.percentage.get();
    }

    public StringProperty categoryProperty() {
        return this.category;
    }

    public DoubleProperty amountProperty() {
        return this.amount;
    }

    public StringProperty percentageProperty() {
        return this.percentage;
    }
}
