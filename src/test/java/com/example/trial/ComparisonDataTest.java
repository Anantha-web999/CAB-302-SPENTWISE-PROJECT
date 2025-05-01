package com.example.trial;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ComparisonDataTest {

    @Test
    void testPositiveDifference() {
        ComparisonData data = new ComparisonData("Shopping", 3000, 4000);
        assertEquals("+1000.0", data.getDifference());
    }

    @Test
    void testNegativeDifference() {
        ComparisonData data = new ComparisonData("Transport", 2500, 2000);
        assertEquals("-500.0", data.getDifference());
    }

    @Test
    void testZeroDifference() {
        ComparisonData data = new ComparisonData("Bills", 1000, 1000);
        assertEquals("0.0", data.getDifference());
    }
}
