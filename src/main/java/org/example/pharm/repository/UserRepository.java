package org.example.pharm.repository;

import org.example.pharm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с {@link User}.
 * Обеспечивает доступ к данным пользователей и предоставляет методы для поиска пользователей по имени или ID.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет пользователя по имени пользователя (username).
     *
     * @param username Имя пользователя.
     * @return Пользователь с заданным именем пользователя.
     */
    User findByUsername(String username);

    /**
     * Ищет пользователя по его идентификатору (ID).
     *
     * @param id Идентификатор пользователя.
     * @return Опционально возвращает пользователя с заданным ID.
     */
    Optional<User> findById(Long id);
}




