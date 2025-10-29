/*
 * Author: Armando Vega
 * Created: 29 October 2025
 * Date Last Modified: 29 October 2025
 * Last Modified By: Armando Vega
 * Summary: User Model POJO Class Unit Test
 */
package com.c11.umastagram.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void testUserCreation() {
        User user = new User();
        // user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        // assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }
}
