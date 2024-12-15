package org.example.pharm.controller;

import org.example.pharm.model.User;
import org.example.pharm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

/**
 * Контроллер для работы с пользователями в системе.
 * <p>
 * Этот контроллер предоставляет RESTful API для получения информации о текущем пользователе.
 * В частности, он позволяет получить данные о пользователе, который в данный момент аутентифицирован в системе.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Получение информации о текущем аутентифицированном пользователе.
     * <p>
     * Этот метод возвращает информацию о пользователе, который в данный момент аутентифицирован в системе.
     * Если пользователь не аутентифицирован, возвращается статус 401 (Unauthorized).
     *
     * @param principal объект, содержащий информацию о текущем пользователе
     * @return ResponseEntity с данными текущего пользователя или статусом 401 (Unauthorized), если пользователь не найден
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
