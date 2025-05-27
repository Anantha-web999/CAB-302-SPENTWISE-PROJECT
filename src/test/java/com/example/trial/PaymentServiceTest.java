package com.example.trial.upcomingpayments;

import org.junit.jupiter.api.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentServiceTest {

    private PaymentService service;

    @BeforeEach
    void setup() {
        service = new PaymentService();
    }

    @Test
    void addPayment_shouldIncreaseListSize() {
        payment p = new payment("Phone", 10.0, LocalDate.now().plusDays(2));
        service.addPayment(p);
        assertEquals(1, service.getPayments().size());
        assertTrue(service.getPayments().contains(p));
    }

    @Test
    void deletePayment_shouldRemoveFromList() {
        payment p = new payment("Water", 20.0, LocalDate.now().plusDays(2));
        service.addPayment(p);
        service.deletePayment(p);
        assertFalse(service.getPayments().contains(p));
    }

    @Test
    void getUpcomingPayments_shouldReturnOnlyFutureUnpaid() {
        payment pastPaid = new payment("Old", 5, LocalDate.now().minusDays(2)); pastPaid.setPaid(true);
        payment upcoming = new payment("New", 30, LocalDate.now().plusDays(2));
        payment futurePaid = new payment("Future Paid", 7, LocalDate.now().plusDays(3)); futurePaid.setPaid(true);
        service.addPayment(pastPaid); service.addPayment(upcoming); service.addPayment(futurePaid);

        var result = service.getUpcomingPayments();
        assertTrue(result.contains(upcoming));
        assertFalse(result.contains(pastPaid));
        assertFalse(result.contains(futurePaid));
    }
}
