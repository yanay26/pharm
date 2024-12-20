package org.example.pharm.service;

import org.example.pharm.model.Product;
import org.example.pharm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для управления продуктами.
 * Обеспечивает операции сохранения, получения, удаления продуктов,
 * а также вычисления статистики по количеству продуктов по датам поставок.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    /**
     * Сохраняет продукт в базе данных.
     *
     * @param product продукт для сохранения.
     * @return сохраненный продукт.
     */
    public Product save(Product product) {
        return repo.save(product);
    }

    /**
     * Получает продукт по его идентификатору.
     *
     * @param id идентификатор продукта.
     * @return продукт с указанным идентификатором, или null, если продукт не найден.
     */
    public Product get(Long id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * Удаляет продукт по его идентификатору.
     *
     * @param id идентификатор продукта, который нужно удалить.
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Получает статистику по количеству продуктов, поставленных в последние 14 дней.
     *
     * @return карта, где ключом является дата поставки, а значением - количество продуктов, поставленных в эту дату.
     *         Продукты учитываются только за последние 14 дней.
     */
    public Map<LocalDate, Long> getProductsCountByDeliveryDate() {
        List<Product> allProducts = repo.findAll();
        Map<LocalDate, Long> countMap = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(14);

        for (Product product : allProducts) {
            LocalDate deliveryDate = product.getDeliveryDate();

            // Учитываем только те продукты, которые были поставлены за последние 14 дней
            if (deliveryDate != null && !deliveryDate.isBefore(startDate) && !deliveryDate.isAfter(today)) {
                // Добавляем количество "В наличии" (quantity) к соответствующей дате поставки
                Long availableCount = (long) product.getQuantity();  // Используем поле quantity
                countMap.put(deliveryDate, countMap.getOrDefault(deliveryDate, 0L) + availableCount);
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



