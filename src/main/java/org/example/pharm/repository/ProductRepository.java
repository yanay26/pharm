package org.example.pharm.repository;

import org.example.pharm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Product}.
 * Этот интерфейс расширяет {@link JpaRepository} и предоставляет методы для выполнения операций CRUD с продуктами.
 * Включает метод для поиска продуктов по ключевому слову.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Ищет продукты, которые соответствуют ключевому слову.
     * Продукты могут быть найдены по совпадению в имени, категории, производителе или дате поставки.
     *
     * @param keyword ключевое слово для поиска продуктов
     * @return список продуктов, которые содержат указанное ключевое слово в одном из полей
     */
    @Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.category.name, ' ', p.manufacturer, ' ', p.deliveryDate) LIKE %?1%")
    List<Product> search(String keyword);

}




