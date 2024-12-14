package org.example.pharm.repository;

import org.example.pharm.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Здесь можно добавить методы поиска категорий, если нужно
}

