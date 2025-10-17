package com.example.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Simple Spring Boot test that only loads the context
@SpringBootTest
class ProductServiceApplicationTests {

    @Test
    void contextLoads() {
        // This test ensures Spring Boot can start without errors
        // No database or repository access here
    }
}