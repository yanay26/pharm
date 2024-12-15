package org.example.pharm.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Сущность, представляющая роль пользователя в системе.
 * Содержит информацию о названии роли и её уникальном идентификаторе.
 */
@Data
@Entity
public class Role {

    /**
     * Уникальный идентификатор роли.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название роли, например, "ROLE_USER", "ROLE_ADMIN".
     * Не может быть пустым (nullable = false).
     */
    @Column(nullable = false)
    private String name;

    /**
     * Конструктор без параметров для создания объекта сущности.
     */
    public Role() {}

}




