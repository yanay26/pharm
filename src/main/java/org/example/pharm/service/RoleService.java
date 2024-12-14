package org.example.pharm.service;

import org.example.pharm.model.Role;
import org.example.pharm.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с ролями пользователей.
 * Включает методы для получения списка всех ролей, поиска роли по ID и имени.
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * Конструктор для инициализации {@link RoleService}.
     *
     * @param roleRepository репозиторий для работы с ролями
     */
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Получает список всех ролей из базы данных.
     *
     * @return список всех ролей
     */
    public List<Role> listAll() {
        return roleRepository.findAll();
    }

    /**
     * Получает роль по её идентификатору.
     * Если роль с указанным ID не найдена, выбрасывается исключение.
     *
     * @param id идентификатор роли
     * @return роль с указанным ID
     * @throws java.util.NoSuchElementException если роль с данным ID не найдена
     */
    public Role get(Long id) {
        return roleRepository.findById(id).get();
    }

    /**
     * Получает роль по её имени.
     * Если роль с указанным именем не найдена, возвращается null.
     *
     * @param name имя роли
     * @return роль с указанным именем, или null, если роль не найдена
     */
    public Role getByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }
}

