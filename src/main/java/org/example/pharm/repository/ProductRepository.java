package org.example.pharm.repository;

import org.example.pharm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Поиск продуктов по ключевому слову
    @Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.category, ' ', p.manufacturer, ' ', p.deliveryDate) LIKE %?1%")
    List<Product> search(String keyword);

    // Расчет средней стоимости продуктов
    @Query("SELECT AVG(p.price) FROM Product p")
    BigDecimal calculateAveragePrice();

    // Расчет средней суммы запасов продуктов
    @Query("SELECT AVG(p.price * p.quantity) FROM Product p WHERE p.quantity > 0")
    BigDecimal calculateAverageStockValue();
}



