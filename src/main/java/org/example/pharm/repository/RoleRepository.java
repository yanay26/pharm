package org.example.pharm.repository;

import org.example.pharm.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Role}.
 * Этот интерфейс расширяет {@link JpaRepository} и предоставляет методы для выполнения операций CRUD с ролями.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит роль по её имени.
     *
     * @param name имя роли
     * @return опциональный объект {@link Optional}, содержащий роль с указанным именем, если она найдена
     */
    Optional<Role> findByName(String name);
}




