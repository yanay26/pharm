package org.example.pharm.service;

import org.example.pharm.model.Product;
import org.example.pharm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для работы с продуктами.
 * Включает методы для получения, сохранения, удаления продуктов,
 * а также для получения статистики по продуктам.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    /**
     * Получает список всех продуктов с возможностью фильтрации по ключевому слову.
     *
     * @param keyword ключевое слово для фильтрации продуктов (может быть null или пустым)
     * @return список продуктов, отфильтрованный по ключевому слову (или все продукты, если фильтр не задан)
     */
    public List<Product> listAll(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return repo.findAll();
        }
        return repo.search(keyword);
    }

    /**
     * Сохраняет продукт в базе данных.
     *
     * @param product продукт, который нужно сохранить
     */
    public void save(Product product) {
        repo.save(product);
    }

    /**
     * Получает продукт по ID.
     *
     * @param id идентификатор продукта
     * @return продукт с указанным ID или null, если продукт не найден
     */
    public Product get(Long id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * Удаляет продукт по ID.
     *
     * @param id идентификатор продукта, который нужно удалить
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Получает количество продуктов по дате поставки за последние 14 дней.
     *
     * @return карта, где ключ - дата поставки, а значение - количество продуктов с этой датой поставки
     */
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
}


