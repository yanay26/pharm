package org.example.pharm.controller;

import org.example.pharm.model.Role;
import org.example.pharm.model.User;
import org.example.pharm.service.RoleService;
import org.example.pharm.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    // Получение списка пользователей
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.listAll();
        return ResponseEntity.ok(users);
    }

    // Удаление пользователя
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Сделать пользователя администратором
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

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}

