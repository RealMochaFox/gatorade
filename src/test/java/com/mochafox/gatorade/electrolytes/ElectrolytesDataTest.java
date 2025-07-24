package com.mochafox.gatorade.electrolytes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ElectrolytesData class.
 */
@ExtendWith(MockitoExtension.class)
class ElectrolytesDataTest {

    @Test
    void testEffectStageCalculationLogic() {
        // Mock the static methods that access the config values
        try (MockedStatic<ElectrolytesData> mockedElectrolytesData = mockStatic(ElectrolytesData.class, CALLS_REAL_METHODS)) {
            // Mock the static methods to return our test values
            mockedElectrolytesData.when(ElectrolytesData::getMaxElectrolytes).thenReturn(1000);
            mockedElectrolytesData.when(ElectrolytesData::getDefaultElectrolytes).thenReturn(1000);
            
            // Verify the effect stage calculation logic
            ElectrolytesData data = new ElectrolytesData();
            
            // Test with default electrolytes (1000)
            assertEquals(3, data.getEffectStage()); 

            // Set electrolytes to a low value and check stage
            data.setElectrolytes(200);
            assertEquals(-1, data.getEffectStage()); 
            
            // Set electrolytes to a critical value and check stage
            data.setElectrolytes(50);
            assertEquals(-3, data.getEffectStage());

            // Set electrolytes to maximum and check stage
            data.setElectrolytes(1000);
            assertEquals(3, data.getEffectStage());
        }
    }

    @Test
    void testAddElectrolytes() {
        try (MockedStatic<ElectrolytesData> mockedElectrolytesData = mockStatic(ElectrolytesData.class, CALLS_REAL_METHODS)) {
            mockedElectrolytesData.when(ElectrolytesData::getMaxElectrolytes).thenReturn(1000);
            mockedElectrolytesData.when(ElectrolytesData::getDefaultElectrolytes).thenReturn(1000);
            
            ElectrolytesData data = new ElectrolytesData();
            
            // Test normal addition
            data.setElectrolytes(500);
            data.addElectrolytes(200);
            assertEquals(700, data.getElectrolytes());
            
            // Test addition that would exceed maximum (should clamp to max)
            data.setElectrolytes(900);
            data.addElectrolytes(200);
            assertEquals(1000, data.getElectrolytes());
            
            // Test adding zero
            data.setElectrolytes(500);
            data.addElectrolytes(0);
            assertEquals(500, data.getElectrolytes());
            
            // Test adding negative value (should decrease)
            data.setElectrolytes(500);
            data.addElectrolytes(-100);
            assertEquals(400, data.getElectrolytes());
        }
    }

    @Test
    void testRemoveElectrolytes() {
        try (MockedStatic<ElectrolytesData> mockedElectrolytesData = mockStatic(ElectrolytesData.class, CALLS_REAL_METHODS)) {
            mockedElectrolytesData.when(ElectrolytesData::getMaxElectrolytes).thenReturn(1000);
            mockedElectrolytesData.when(ElectrolytesData::getDefaultElectrolytes).thenReturn(1000);
            
            ElectrolytesData data = new ElectrolytesData();
            
            // Test normal removal
            data.setElectrolytes(500);
            data.removeElectrolytes(200);
            assertEquals(300, data.getElectrolytes());
            
            // Test removal that would go below zero (should clamp to 0)
            data.setElectrolytes(100);
            data.removeElectrolytes(200);
            assertEquals(0, data.getElectrolytes());
            
            // Test removing zero
            data.setElectrolytes(500);
            data.removeElectrolytes(0);
            assertEquals(500, data.getElectrolytes());
            
            // Test removing negative value (should increase)
            data.setElectrolytes(500);
            data.removeElectrolytes(-100);
            assertEquals(600, data.getElectrolytes());
        }
    }

    @Test
    void testGetElectrolytePercentage() {
        try (MockedStatic<ElectrolytesData> mockedElectrolytesData = mockStatic(ElectrolytesData.class, CALLS_REAL_METHODS)) {
            mockedElectrolytesData.when(ElectrolytesData::getMaxElectrolytes).thenReturn(1000);
            mockedElectrolytesData.when(ElectrolytesData::getDefaultElectrolytes).thenReturn(1000);
            
            ElectrolytesData data = new ElectrolytesData();
            
            // Test full electrolytes (100%)
            data.setElectrolytes(1000);
            assertEquals(1.0f, data.getElectrolytePercentage(), 0.001f);
            
            // Test half electrolytes (50%)
            data.setElectrolytes(500);
            assertEquals(0.5f, data.getElectrolytePercentage(), 0.001f);
            
            // Test quarter electrolytes (25%)
            data.setElectrolytes(250);
            assertEquals(0.25f, data.getElectrolytePercentage(), 0.001f);
            
            // Test empty electrolytes (0%)
            data.setElectrolytes(0);
            assertEquals(0.0f, data.getElectrolytePercentage(), 0.001f);
            
            // Test specific values
            data.setElectrolytes(750);
            assertEquals(0.75f, data.getElectrolytePercentage(), 0.001f);
        }
    }

    @Test
    void testIsLow() {
        try (MockedStatic<ElectrolytesData> mockedElectrolytesData = mockStatic(ElectrolytesData.class, CALLS_REAL_METHODS)) {
            mockedElectrolytesData.when(ElectrolytesData::getMaxElectrolytes).thenReturn(1000);
            mockedElectrolytesData.when(ElectrolytesData::getDefaultElectrolytes).thenReturn(1000);
            
            ElectrolytesData data = new ElectrolytesData();
            
            // Test just above low threshold (29% = 290)
            data.setElectrolytes(290);
            assertFalse(data.isLow());
            
            // Test exactly at low threshold (29% = 290)
            data.setElectrolytes(289);
            assertTrue(data.isLow());
            
            // Test well below low threshold
            data.setElectrolytes(100);
            assertTrue(data.isLow());
            
            // Test at zero
            data.setElectrolytes(0);
            assertTrue(data.isLow());
            
            // Test well above threshold
            data.setElectrolytes(500);
            assertFalse(data.isLow());
            
            // Test at maximum
            data.setElectrolytes(1000);
            assertFalse(data.isLow());
        }
    }

    @Test
    void testIsCritical() {
        try (MockedStatic<ElectrolytesData> mockedElectrolytesData = mockStatic(ElectrolytesData.class, CALLS_REAL_METHODS)) {
            mockedElectrolytesData.when(ElectrolytesData::getMaxElectrolytes).thenReturn(1000);
            mockedElectrolytesData.when(ElectrolytesData::getDefaultElectrolytes).thenReturn(1000);
            
            ElectrolytesData data = new ElectrolytesData();
            
            // Test just above critical threshold (10% = 100)
            data.setElectrolytes(100);
            assertFalse(data.isCritical());
            
            // Test exactly at critical threshold (10% = 100)
            data.setElectrolytes(99);
            assertTrue(data.isCritical());
            
            // Test well below critical threshold
            data.setElectrolytes(50);
            assertTrue(data.isCritical());
            
            // Test at zero
            data.setElectrolytes(0);
            assertTrue(data.isCritical());
            
            // Test well above threshold
            data.setElectrolytes(500);
            assertFalse(data.isCritical());
            
            // Test at maximum
            data.setElectrolytes(1000);
            assertFalse(data.isCritical());
        }
    }

}
