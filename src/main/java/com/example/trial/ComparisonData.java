//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.trial;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ComparisonData {
    private final StringProperty category;
    private final DoubleProperty month1Value;
    private final DoubleProperty month2Value;
    private final StringProperty difference;

    public ComparisonData(String category, double m1, double m2) {
        this.category = new SimpleStringProperty(category);
        this.month1Value = new SimpleDoubleProperty(m1);
        this.month2Value = new SimpleDoubleProperty(m2);
        double diff = m2 - m1;
        String diffText = (diff > (double)0.0F ? "+" : "") + diff;
        this.difference = new SimpleStringProperty(diffText);
    }

    public String getCategory() {
        return (String)this.category.get();
    }

    public double getMonth1Value() {
        return this.month1Value.get();
    }

    public double getMonth2Value() {
        return this.month2Value.get();
    }

    public String getDifference() {
        return (String)this.difference.get();
    }

    public StringProperty categoryProperty() {
        return this.category;
    }

    public DoubleProperty month1ValueProperty() {
        return this.month1Value;
    }

    public DoubleProperty month2ValueProperty() {
        return this.month2Value;
    }

    public StringProperty differenceProperty() {
        return this.difference;
    }
}
