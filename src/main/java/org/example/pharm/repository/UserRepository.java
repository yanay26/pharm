package org.example.pharm.repository;

import org.example.pharm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User}.
 * Этот интерфейс расширяет {@link JpaRepository} и предоставляет методы для выполнения операций CRUD с пользователями.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по его имени пользователя (логину).
     *
     * @param username имя пользователя (логин)
     * @return найденный пользователь или {@code null}, если пользователь не найден
     */
    User findByUsername(String username);

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return опциональный объект {@link Optional}, содержащий пользователя, если он найден
     */
    Optional<User> findById(Long id);
}



