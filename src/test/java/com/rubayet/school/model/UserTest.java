package com.rubayet.school.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getAndSetId() {
        User user = new User();
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void getAndSetUsername() {
        User user = new User();
        user.setUsername("john");
        assertEquals("john", user.getUsername());
    }

    @Test
    void getAndSetPassword() {
        User user = new User();
        user.setPassword("secret");
        assertEquals("secret", user.getPassword());
    }

    @Test
    void getAndSetRole() {
        User user = new User();
        user.setRole("ROLE_STUDENT");
        assertEquals("ROLE_STUDENT", user.getRole());
    }

    @Test
    void testEquals() {
        User u1 = new User();
        u1.setId(1L);

        User u2 = new User();
        u2.setId(1L);

        assertEquals(u1, u2);
    }

    @Test
    void testHashCode() {
        User u1 = new User();
        u1.setId(1L);

        User u2 = new User();
        u2.setId(1L);

        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testToString() {
        User user = new User();
        user.setUsername("nabil");
        assertNotNull(user.toString());
    }
}