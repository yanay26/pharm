package org.example.pharm.service;

import jakarta.transaction.Transactional;
import org.example.pharm.model.Role;
import org.example.pharm.model.User;
import org.example.pharm.repository.RoleRepository;
import org.example.pharm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис для управления пользователями.
 * Обеспечивает операции регистрации, поиска, сохранения, удаления пользователей,
 * а также назначение ролей пользователям.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Получить список всех пользователей.
     *
     * @return список всех пользователей.
     */
    public List<User> listAll() {
        return userRepository.findAll(); // Просто возвращаем всех пользователей из репозитория
    }

    /**
     * Регистрация нового пользователя.
     * Шифрует пароль пользователя, назначает роль "ROLE_USER" и сохраняет пользователя в базе данных.
     * Если роль "ROLE_USER" не найдена, она будет создана.
     *
     * @param user пользователь для регистрации.
     * @return зарегистрированный пользователь.
     */
    @Transactional
    public User registerUser(User user) {
        // Проверяем, существует ли пользователь с таким email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Этот email уже используется. Пожалуйста, введите другой.");
        }

        // Шифруем пароль пользователя
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Получаем роль "ROLE_USER" или создаем её, если она не найдена
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Роль ROLE_USER не найдена"));
        if (role == null) {
            role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
        }

        // Присваиваем роль пользователю
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        // Сохраняем пользователя с присвоенной ролью
        return userRepository.save(user);
    }


    /**
     * Найти пользователя по имени пользователя.
     *
     * @param username имя пользователя.
     * @return пользователь с указанным именем.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Получить пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return пользователь с указанным идентификатором.
     * @throws RuntimeException если пользователь не найден.
     */
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден."));
    }

    /**
     * Удалить пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Назначить роль "ROLE_ADMIN" пользователю.
     * Если роль "ROLE_ADMIN" не найдена, выбрасывается исключение.
     *
     * @param userId идентификатор пользователя, которому необходимо назначить роль.
     * @throws IllegalArgumentException если роль "ROLE_ADMIN" не найдена.
     */
    public void assignRoleToAdmin(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Получаем роль ROLE_ADMIN, если она не найдена, выбрасываем исключение
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new IllegalArgumentException("Роль ADMIN не найдена"));

            user.getRoles().clear(); // Убираем текущие роли, если требуется
            user.getRoles().add(adminRole); // Добавляем роль ROLE_ADMIN

            // Сохраняем обновленного пользователя
            userRepository.save(user);
        }
    }
}
