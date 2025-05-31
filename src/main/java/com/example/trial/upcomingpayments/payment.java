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


     */
    public payment(String name, double amount, LocalDate dueDate) {
        setName(name);           // Set the payment name
        setAmount(amount);       // Set the payment amount
        setDueDate(dueDate);     // Set the payment due date
        setPaid(false);          // Payments are unpaid by default
    }

    // Property getters - these are used by JavaFX to bind UI elements


    public StringProperty nameProperty() { return name; }

    /**
     * Returns the property for the payment's amount
     */
    public DoubleProperty amountProperty() { return amount; }

    /**
     * Returns the property for the payment's due date
     */
    public ObjectProperty<LocalDate> dueDateProperty() { return dueDate; }

    /**
     *  Returns the property for whether this payment has been paid
     */
    public BooleanProperty paidProperty() { return paid; }

    // Setters to update the properties' values

    /**
     * Sets the name for this payment.
     */
    public final void setName(String value) { name.set(value); }

    /**
     * Sets the amount for this payment.
     */
    public final void setAmount(double value) { amount.set(value); }

    /**
     * Sets the due date for this payment.
     */
    public final void setDueDate(LocalDate value) { dueDate.set(value); }

    /**
     * Sets whether this payment has been paid.
     */
    public final void setPaid(boolean value) { paid.set(value); }
}

