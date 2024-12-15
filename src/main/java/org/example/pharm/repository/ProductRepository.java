package org.example.pharm.repository;

import org.example.pharm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для работы с {@link Product}.
 * Предоставляет стандартные CRUD-операции и метод для поиска продуктов по ключевому слову.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Ищет продукты по ключевому слову в полях: название, категория, производитель, дата поставки.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список найденных продуктов.
     */
    @Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.category.name, ' ', p.manufacturer, ' ', p.deliveryDate) LIKE %?1%")
    List<Product> search(String keyword);
}





