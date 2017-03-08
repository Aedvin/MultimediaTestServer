package Log;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class LogTest {
    @AfterEach
    void tearDown() {
        Log.close();
    }

    @BeforeEach
    void setUp() {
        Log.init("test/");
    }

    @org.junit.jupiter.api.Test
    void writeLogEntry() {
        Log.writeLogEntry("Test", "Test");
    }

    @org.junit.jupiter.api.Test
    void writeException() {
        try {
            throw new Exception("Test");
        } catch (Exception e) {
            Log.writeException("Exception test", e);
        }
    }

}