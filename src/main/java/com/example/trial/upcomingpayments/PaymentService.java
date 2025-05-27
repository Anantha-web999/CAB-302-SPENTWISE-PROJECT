package com.example.trial.upcomingpayments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentService {
    private final ObservableList<payment> payments = FXCollections.observableArrayList();

    public ObservableList<payment> getPayments() {
        return payments;
    }

    public void addPayment(payment p) {
        payments.add(p);
    }

    public void deletePayment(payment p) {
        payments.remove(p);
    }

    public List<payment> getUpcomingPayments() {
        return payments.stream()
                .filter(pay -> !pay.paidProperty().get() && !pay.dueDateProperty().get().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }
}
