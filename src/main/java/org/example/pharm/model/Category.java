package org.example.pharm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Модель категории продуктов.
 * <p>
 * Этот класс представляет сущность "Category", которая используется для хранения информации о категориях продуктов
 * в системе аптеки. Каждая категория имеет уникальный идентификатор и имя.
 */
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Генерация идентификатора через автоинкремент
    private Long id;

    private String name;

    /**
     * Конструктор по умолчанию.
     * <p>
     * Создает новый объект категории без значений для полей.
     */
    public Category() {
    }

    /**
     * Получение идентификатора категории.
     *
     * @return Идентификатор категории
     */
    public Long getId() {
        return id;
    }

    /**
     * Установка идентификатора категории.
     *
     * @param id Идентификатор категории
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получение имени категории.
     *
     * @return Имя категории
     */
    public String getName() {
        return name;
    }

    /**
     * Установка имени категории.
     *
     * @param name Имя категории
     */
    public void setName(String name) {
        this.name = name;
    }
}


