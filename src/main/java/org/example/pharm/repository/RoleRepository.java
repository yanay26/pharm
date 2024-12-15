package org.example.pharm.repository;

import org.example.pharm.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с {@link Role}.
 * Обеспечивает доступ к данным ролей и предоставляет методы для поиска роли по имени.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Ищет роль по имени.
     *
     * @param name Имя роли.
     * @return Опционально возвращает роль с заданным именем.
     */
    Optional<Role> findByName(String name);
}





