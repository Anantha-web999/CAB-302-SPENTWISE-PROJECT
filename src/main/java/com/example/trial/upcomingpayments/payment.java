package com.example.trial.upcomingpayments;

import javafx.beans.property.*;

import java.time.LocalDate;


public class payment {
    // Name of the payment (e.g. "Electricity Bill")
    private final StringProperty name = new SimpleStringProperty();
    // Amount of money to be paid
    private final DoubleProperty amount = new SimpleDoubleProperty();
    // The date the payment is due
    private final ObjectProperty<LocalDate> dueDate = new SimpleObjectProperty<>();
    // Whether this payment has been paid or not
    private final BooleanProperty paid = new SimpleBooleanProperty();


    public payment(String name, double amount, LocalDate dueDate) {
        setName(name);           // Set the payment name
        setAmount(amount);       // Set the payment amount
        setDueDate(dueDate);     // Set the payment due date
        setPaid(false);          // Payments are unpaid by default
    }




    public StringProperty nameProperty() { return name; }

    public DoubleProperty amountProperty() { return amount; }


    public ObjectProperty<LocalDate> dueDateProperty() { return dueDate; }


    public BooleanProperty paidProperty() { return paid; }

    // Setters to update the properties' values


    public final void setName(String value) { name.set(value); }


    public final void setAmount(double value) { amount.set(value); }


    public final void setDueDate(LocalDate value) { dueDate.set(value); }


    public final void setPaid(boolean value) { paid.set(value); }
}

