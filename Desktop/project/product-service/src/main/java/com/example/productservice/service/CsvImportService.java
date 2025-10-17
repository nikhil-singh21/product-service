package com.example.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.productservice.entity.*;
import com.example.productservice.repository.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CsvImportService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public void importData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String productCode = data[0];
                String productName = data[1];
                String categoryCode = data[2];
                String categoryName = data[3];

                // Check or create category
                Category category = categoryRepository.findByCategoryCode(categoryCode)
                        .orElseGet(() -> {
                            Category c = new Category();
                            c.setCategoryCode(categoryCode);
                            c.setCategoryName(categoryName);
                            return categoryRepository.save(c);
                        });

                // Check if product already exists
                Optional<Product> existing = productRepository.findByProductCode(productCode);
                if (existing.isEmpty()) {
                    Product product = new Product();
                    product.setProductCode(productCode);
                    product.setProductName(productName);
                    product.setCategory(category);
                    productRepository.save(product);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV: " + e.getMessage(), e);
        }
    }
}
