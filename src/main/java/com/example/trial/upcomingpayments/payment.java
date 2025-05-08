package com.example.trial.upcomingpayments;

import javafx.beans.property.*;

import java.time.LocalDate;

public class payment {
    private final StringProperty name = new SimpleStringProperty();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDate> dueDate = new SimpleObjectProperty<>();
    private final BooleanProperty paid = new SimpleBooleanProperty();

    public payment(String name, double amount, LocalDate dueDate) {
        setName(name);
        setAmount(amount);
        setDueDate(dueDate);
        setPaid(false);
    }

    public StringProperty nameProperty() { return name; }
    public DoubleProperty amountProperty() { return amount; }
    public ObjectProperty<LocalDate> dueDateProperty() { return dueDate; }
    public BooleanProperty paidProperty() { return paid; }

    public final void setName(String value) { name.set(value); }
    public final void setAmount(double value) { amount.set(value); }
    public final void setDueDate(LocalDate value) { dueDate.set(value); }
    public final void setPaid(boolean value) { paid.set(value); }
}
