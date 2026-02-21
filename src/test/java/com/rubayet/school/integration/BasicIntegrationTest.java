package com.rubayet.school.integration;

import com.rubayet.school.controller.HomeController;
import com.rubayet.school.controller.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BasicIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testApplicationContextLoads() {
        // Verifies that the application context starts successfully
        assertNotNull(applicationContext);
    }

    @Test
    void testControllersAreLoaded() {
        // Verifies that key controllers are present in the context
        assertNotNull(applicationContext.getBean(HomeController.class));
        assertNotNull(applicationContext.getBean(LoginController.class));
    }
}
