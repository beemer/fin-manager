package com.finmanager;

import org.junit.Test;
import static org.junit.Assert.*;

public class HelloWorldTest {
    @Test
    public void testGetMessage() {
        assertEquals("Hello, World!", HelloWorld.getMessage());
    }

    @Test
    public void testGetMessageNotNull() {
        assertNotNull(HelloWorld.getMessage());
    }

    @Test
    public void testGetMessageNotEmpty() {
        assertFalse(HelloWorld.getMessage().isEmpty());
    }
}
