package org.example.pharm.service;

import org.example.pharm.model.Role;
import org.example.pharm.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Возвращает список всех ролей из базы данных.
    public List<Role> listAll() {
        return roleRepository.findAll();
    }

    // Возвращает роль по её идентификатору. Если роль не найдена, выбрасывается исключение.
    public Role get(Long id) {
        return roleRepository.findById(id).get();
    }

    public Role getByName(String name) {
        return roleRepository.findByName(name).orElse(null); // если роль не найдена, возвращаем null
    }
}

