package org.example.pharm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Модель продукта.
 * <p>
 * Этот класс представляет сущность "Product", которая используется для хранения информации о продукте в системе аптеки.
 * Каждый продукт имеет уникальный идентификатор, название, категорию, производителя, цену, количество на складе и дату поставки.
 */
@Entity
public class Product {

    private Long id; // ID продукта
    private String name; // Название продукта
    private Category category; // Категория продукта (ссылка на объект категории)
    private String manufacturer; // Производитель
    private BigDecimal price; // Цена продукта
    private Integer quantity; // Количество на складе
    private LocalDate deliveryDate; // Дата поставки

    /**
     * Конструктор по умолчанию.
     * <p>
     * Создает новый объект продукта без значений для полей.
     */
    public Product() {
    }

    /**
     * Получение идентификатора продукта.
     *
     * @return Идентификатор продукта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Генерация идентификатора через автоинкремент
    public Long getId() {
        return id;
    }

    /**
     * Установка идентификатора продукта.
     *
     * @param id Идентификатор продукта
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получение названия продукта.
     *
     * @return Название продукта
     */
    @Column(nullable = false, length = 255) // Указываем обязательность и максимальную длину
    public String getName() {
        return name;
    }

    /**
     * Установка названия продукта.
     *
     * @param name Название продукта
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получение категории продукта.
     *
     * @return Категория продукта
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) // Связь с категорией, обязательность
    public Category getCategory() {
        return category;
    }

    /**
     * Установка категории продукта.
     *
     * @param category Категория продукта
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Получение производителя продукта.
     *
     * @return Производитель продукта
     */
    @Column(nullable = false, length = 255) // Указываем обязательность и максимальную длину
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Установка производителя продукта.
     *
     * @param manufacturer Производитель продукта
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Получение цены продукта.
     *
     * @return Цена продукта
     */
    @Column(nullable = false) // Указываем обязательность
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Установка цены продукта.
     *
     * @param price Цена продукта
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Получение количества продукта на складе.
     *
     * @return Количество продукта
     */
    @Column(nullable = false) // Указываем обязательность
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Установка количества продукта на складе.
     *
     * @param quantity Количество продукта
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Получение даты поставки продукта.
     *
     * @return Дата поставки
     */
    @Column(nullable = false) // Указываем обязательность
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Установка даты поставки продукта.
     *
     * @param deliveryDate Дата поставки
     */
    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}



