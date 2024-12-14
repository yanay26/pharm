package org.example.pharm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сущность, представляющая продукт в аптечном магазине.
 * Хранит информацию о продукте, такую как его название, категория, производитель, цена, количество на складе и дата поставки.
 */
@Entity
public class Product {

    private Long id; // ID продукта
    private String name; // Название продукта
    private String category; // Категория продукта
    private String manufacturer; // Производитель
    private BigDecimal price; // Цена продукта
    private Integer quantity; // Количество на складе
    private LocalDate deliveryDate; // Дата поставки

    /**
     * Конструктор без параметров, необходимый для создания объекта сущности.
     */
    public Product() {
    }

    /**
     * Получение ID продукта.
     *
     * @return ID продукта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    /**
     * Установка ID продукта.
     *
     * @param id ID продукта
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получение названия продукта.
     *
     * @return Название продукта
     */
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
    public String getCategory() {
        return category;
    }

    /**
     * Установка категории продукта.
     *
     * @param category Категория продукта
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Получение производителя продукта.
     *
     * @return Производитель продукта
     */
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
     * @return Количество продукта на складе
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Установка количества продукта на складе.
     *
     * @param quantity Количество продукта на складе
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Получение даты поставки продукта.
     *
     * @return Дата поставки продукта
     */
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Установка даты поставки продукта.
     *
     * @param deliveryDate Дата поставки продукта
     */
    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * Возвращает строковое представление продукта, включая все его атрибуты.
     *
     * @return Строковое представление продукта
     */
    @Override
    public String toString() {
        return "Product [id=" + id +
                ", name=" + name +
                ", category=" + category +
                ", manufacturer=" + manufacturer +
                ", price=" + price +
                ", quantity=" + quantity +
                ", deliveryDate=" + deliveryDate + "]";
    }
}



