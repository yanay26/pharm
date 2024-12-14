package org.example.pharm.controller;

import org.example.pharm.model.Product;
import org.example.pharm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления продуктами через API.
 * Обрабатывает запросы для получения, создания, обновления и удаления продуктов,
 * а также предоставляет данные для гистограммы по дате доставки.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Получение списка всех продуктов.
     *
     * @param keyword Ключевое слово для фильтрации продуктов по названию.
     * @return Список продуктов, соответствующих критериям поиска.
     */
    @GetMapping
    public List<Product> getAllProducts(@RequestParam(value = "keyword", required = false) String keyword) {
        return productService.listAll(keyword);
    }

    /**
     * Получение продукта по его идентификатору.
     *
     * @param id Идентификатор продукта.
     * @return Продукт с указанным идентификатором, если он найден, иначе 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.get(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    /**
     * Создание нового продукта.
     *
     * @param product Новый продукт, который будет создан.
     * @return Ответ с кодом 201 и созданным продуктом.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        productService.save(product);
        return ResponseEntity.status(201).body(product);
    }

    /**
     * Обновление существующего продукта.
     *
     * @param id Идентификатор продукта, который требуется обновить.
     * @param product Обновленные данные продукта.
     * @return Ответ с обновленным продуктом, если он найден, иначе 404 Not Found.
     */
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

    /**
     * Удаление продукта по его идентификатору.
     *
     * @param id Идентификатор продукта, который требуется удалить.
     * @return Ответ с кодом 204 (No Content) при успешном удалении.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение данных для гистограммы продуктов по дате доставки.
     *
     * @return Карта, где ключ - дата доставки, а значение - количество продуктов.
     */
    @GetMapping("/histogram/data")
    public Map<LocalDate, Long> getHistogramData() {
        return productService.getProductsCountByDeliveryDate();
    }

}