package com.example.trial;

import static org.junit.jupiter.api.Assertions.*;

import com.example.trial.insights_AI.CategoryData;
import org.junit.jupiter.api.Test;

class CategoryDataTest {

    @Test
    void testCategoryDataCreation() {
        CategoryData data = new CategoryData("Food", 5000, "25%");
        assertEquals("Food", data.getCategory());
        assertEquals(5000, data.getAmount());
        assertEquals("25%", data.getPercentage());
    }
}
