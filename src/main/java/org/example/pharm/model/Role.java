package org.example.pharm.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Role() {}

    @PrePersist
    public void ensureRoleName() {
        if (this.name == null) {
            this.name = "ROLE_USER"; // по умолчанию роль "USER", если не указано другое
        }
    }
}



