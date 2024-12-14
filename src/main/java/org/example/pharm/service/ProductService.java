package org.example.pharm.service;

import org.example.pharm.model.Product;
import org.example.pharm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    // Список всех продуктов с возможностью поиска
    public List<Product> listAll(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return repo.findAll();
        }
        return repo.search(keyword);

    }

    // Сохранение продукта
    public void save(Product product) {
        repo.save(product);
    }

    // Получение продукта по ID
    public Product get(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Удаление продукта по ID
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // Получение количества продуктов по дате поставки за последние 14 дней
    public Map<LocalDate, Long> getProductsCountByDeliveryDate() {
        List<Product> allProducts = repo.findAll();
        Map<LocalDate, Long> countMap = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(14);

        for (Product product : allProducts) {
            LocalDate deliveryDate = product.getDeliveryDate();

            // Учитываем продукты только за последние 14 дней
            if (deliveryDate != null && !deliveryDate.isBefore(startDate) && !deliveryDate.isAfter(today)) {
                countMap.put(deliveryDate, countMap.getOrDefault(deliveryDate, 0L) + 1);
            }
        }

        // Сортировка по дате в порядке возрастания
        return countMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    // Получение средней стоимости продуктов
    public BigDecimal getAveragePrice() {
        return repo.calculateAveragePrice();
    }

    // Получение средней стоимости запасов продуктов
    //public BigDecimal getAverageStockValue() {
    //    return repo.calculateAverageStockValue();
    //}
}


