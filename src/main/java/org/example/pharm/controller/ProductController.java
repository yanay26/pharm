package org.example.pharm.controller;

import org.example.pharm.model.Product;
import org.example.pharm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Получение списка продуктов
    @GetMapping
    public List<Product> getAllProducts(@RequestParam(value = "keyword", required = false) String keyword) {
        return productService.listAll(keyword);
    }

    // Получение продукта по ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.get(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // Создание нового продукта
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        productService.save(product);
        return ResponseEntity.status(201).body(product);
    }

    // Обновление продукта
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product existingProduct = productService.get(id);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        productService.save(existingProduct);
        return ResponseEntity.ok(existingProduct);
    }

    // Удаление продукта
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/histogram/data")
    public Map<LocalDate, Long> getHistogramData() {
        return productService.getProductsCountByDeliveryDate();
    }

}