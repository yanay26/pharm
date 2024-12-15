package org.example.pharm.controller;

import org.example.pharm.model.Category;
import org.example.pharm.model.Product;
import org.example.pharm.repository.ProductRepository;
import org.example.pharm.service.CategoryService;
import org.example.pharm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для работы с продуктами в системе.
 * <p>
 * Этот контроллер предоставляет RESTful API для управления продуктами, включая операции добавления,
 * обновления, удаления, получения данных о продукте, а также получения гистограммы по датам поставок.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Получение списка продуктов с возможностью фильтрации по ключевому слову.
     * <p>
     * Если параметр "keyword" не передан или пуст, возвращаются все продукты.
     * В противном случае выполняется поиск по ключевому слову.
     *
     * @param keyword ключевое слово для фильтрации продуктов (необязательный параметр)
     * @return ResponseEntity, содержащий список продуктов
     */
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestParam(value = "keyword", required = false) String keyword) {
        List<Product> products;
        if (keyword == null || keyword.isEmpty()) {
            products = productRepository.findAll();
        } else {
            products = productRepository.search(keyword);
        }
        return ResponseEntity.ok(products);
    }

    /**
     * Получение данных о продукте по его идентификатору.
     * <p>
     * Если продукт с указанным идентификатором найден, возвращается его информация.
     * Если продукт не найден, возвращается ошибка 404 (Not Found).
     *
     * @param id идентификатор продукта
     * @return ResponseEntity, содержащий данные продукта или статус 404, если продукт не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.get(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    /**
     * Добавление нового продукта в систему.
     * <p>
     * Если у продукта указана новая категория (без идентификатора), она сохраняется в базе данных,
     * а затем продукт сохраняется с привязанной категорией.
     *
     * @param product объект продукта, который нужно добавить
     * @return ResponseEntity, содержащий добавленный продукт и статус 201 (Created), или ошибку 500 при проблемах
     */
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Category category = product.getCategory();
            if (category != null && category.getId() == null) {
                category = categoryService.save(category);
                product.setCategory(category);
            }
            Product savedProduct = productService.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Обновление информации о продукте.
     * <p>
     * Если продукт с указанным идентификатором найден, его данные обновляются.
     * В противном случае возвращается ошибка 404 (Not Found).
     *
     * @param id идентификатор продукта, который нужно обновить
     * @param product объект с новыми данными для обновления
     * @return ResponseEntity, содержащий обновленный продукт или статус 404, если продукт не найден
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product existingProduct = productService.get(id);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        existingProduct.setName(product.getName());
        existingProduct.setManufacturer(product.getManufacturer());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setDeliveryDate(product.getDeliveryDate());

        if (product.getCategory() != null) {
            Category category = product.getCategory();
            if (category.getId() == null) {
                category = categoryService.save(category);
            } else {
                category = categoryService.findById(category.getId());
            }
            existingProduct.setCategory(category);
        }

        productService.save(existingProduct);
        return ResponseEntity.ok(existingProduct);
    }

    /**
     * Удаление продукта из системы.
     * <p>
     * Продукт с указанным идентификатором удаляется из базы данных.
     * Если продукт найден и успешно удалён, возвращается статус 204 (No Content).
     *
     * @param id идентификатор продукта, который нужно удалить
     * @return ResponseEntity с кодом ответа 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение данных для отображения гистограммы по датам поставок продуктов.
     * <p>
     * Возвращает количество продуктов, поставленных в каждый день, в виде карты с датами и количеством.
     *
     * @return карта, где ключ — это дата поставки, а значение — количество продуктов, поставленных в этот день
     */
    @GetMapping("/histogram")
    public Map<LocalDate, Long> getHistogramData() {
        return productService.getProductsCountByDeliveryDate();
    }
}
