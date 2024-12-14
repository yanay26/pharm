package org.example.pharm.controller;

import org.example.pharm.model.Role;
import org.example.pharm.model.User;
import org.example.pharm.service.RoleService;
import org.example.pharm.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Контроллер для управления пользователями.
 * Предоставляет RESTful API для получения списка пользователей, удаления пользователя,
 * назначения роли администратора и получения текущего пользователя.
 * Доступ к методам контроллера ограничен ролями через Spring Security.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * Получение списка всех пользователей.
     * Доступно только пользователю с ролью ADMIN.
     *
     * @return Список всех пользователей в формате JSON
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.listAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Удаление пользователя по его ID.
     * Доступно только пользователю с ролью ADMIN.
     *
     * @param id ID пользователя, которого нужно удалить
     * @return Статус ответа HTTP
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Назначение роли администратора пользователю по его ID.
     * Доступно только пользователю с ролью ADMIN.
     *
     * @param id ID пользователя, которому нужно присвоить роль администратора
     * @return Обновленный пользователь в формате JSON
     */
    @PutMapping("/{id}/makeAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> makeUserAdmin(@PathVariable Long id) {
        User user = userService.get(id);  // Получаем пользователя по ID
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Очищаем старые роли
        user.getRoles().clear();

        // Получаем роль ROLE_ADMIN
        Role adminRole = roleService.getByName("ROLE_ADMIN");
        if (adminRole == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Добавляем роль ROLE_ADMIN
        user.getRoles().add(adminRole);
        userService.save(user);  // Сохраняем изменения

        return ResponseEntity.ok(user);
    }

    /**
     * Получение информации о текущем аутентифицированном пользователе.
     * Возвращает данные о пользователе, если он аутентифицирован.
     *
     * @param principal Информация о текущем пользователе из контекста безопасности
     * @return Данные о текущем пользователе в формате JSON, либо статус UNAUTHORIZED
     */
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}

