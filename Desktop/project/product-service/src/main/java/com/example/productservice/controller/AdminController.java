package com.example.productservice.controller;

import com.example.productservice.service.CsvImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CsvImportService csvImportService;

    @PostMapping("/import-csv")
    public ResponseEntity<String> importCsv(@RequestParam(required = false) String path) {
        String importPath = path == null ? "src/main/resources/TestExampleFile.csv" : path;
        File f = new File(importPath);
        if (!f.exists() || !f.isFile()) {
            return ResponseEntity.badRequest().body("CSV file not found at: " + importPath);
        }
        try {
            csvImportService.importData(importPath);
            return ResponseEntity.ok("Import started for: " + importPath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Import failed: " + e.getMessage());
        }
    }
}
