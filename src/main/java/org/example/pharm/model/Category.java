package org.example.pharm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Category {

    private Long id; // ID категории
    private String name; // Название категории

    public Category() {
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

