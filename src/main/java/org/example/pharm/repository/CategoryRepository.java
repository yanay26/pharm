package org.example.pharm.repository;

import org.example.pharm.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью {@link Category}.
 * <p>
 * Этот интерфейс предоставляет стандартные CRUD-операции для работы с объектами {@link Category},
 * такими как сохранение, обновление, удаление и извлечение категорий из базы данных.
 * Репозиторий наследует от {@link JpaRepository}, что позволяет использовать множество готовых методов для работы с JPA.
 *
 * @see Category
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

}


