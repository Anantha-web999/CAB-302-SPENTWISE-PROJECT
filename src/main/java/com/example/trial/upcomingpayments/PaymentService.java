package com.example.trial.upcomingpayments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PaymentService acts as the manager for all payments.
 * It holds the main list of payments and provides methods to add, delete,
 * and filter payments for the rest of the app to use.
 */
public class PaymentService {
    // This list holds all the payment objects and allows the UI to react when it changes.
    private final ObservableList<payment> payments = FXCollections.observableArrayList();

    /**
     * Returns the full list of payments.
     * The ObservableList makes it easy to connect to the TableView in the UI.
     */
    public ObservableList<payment> getPayments() {
        return payments;
    }

    /**
     * Adds a new payment to the list.
     * @param p The payment to add
     */
    public void addPayment(payment p) {
        payments.add(p);
    }

    /**
     * Removes a payment from the list.
     * @param p The payment to delete
     */
    public void deletePayment(payment p) {
        payments.remove(p);
    }

    /**
     * Returns a list of upcoming payments.
     * These are payments that:
     *  - Have NOT been marked as paid
     *  - Have a due date that is today or in the future (not in the past)
     *
     * @return List of upcoming, unpaid payments
     */
    public List<payment> getUpcomingPayments() {
        return payments.stream()
                .filter(pay ->
                        // Only include if payment is NOT paid
                        !pay.paidProperty().get()
                                // And the due date is today or after today
                                && !pay.dueDateProperty().get().isBefore(LocalDate.now())
                )
                .collect(Collectors.toList());
    }
}
