package com.example.productservice;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        // Normalize JVM timezone to a canonical ID and set system property so
        // the Postgres driver and other libraries see a consistent, accepted
        // value. This protects against platform/OS zone aliases like
        // "Asia/Calcutta" which some Postgres installations reject.
        String currentTz = TimeZone.getDefault().getID();
        String normalizedTz = currentTz;
        // Map known legacy aliases to canonical zone IDs. Add more if needed.
        if ("Asia/Calcutta".equalsIgnoreCase(currentTz)) {
            normalizedTz = "Asia/Kolkata";
        }
        // As a safe fallback, prefer UTC if normalization fails to produce a
        // valid zone for getTimeZone
        if (TimeZone.getTimeZone(normalizedTz) == null || "GMT".equals(TimeZone.getTimeZone(normalizedTz).getID()) && !"GMT".equalsIgnoreCase(normalizedTz)) {
            normalizedTz = "UTC";
        }
        System.setProperty("user.timezone", normalizedTz);
        TimeZone.setDefault(TimeZone.getTimeZone(normalizedTz));

        SpringApplication.run(ProductServiceApplication.class, args);
    }

    // CSV import at startup removed to make startup deterministic. Use the
    // /admin/import-csv endpoint to trigger imports manually.
}