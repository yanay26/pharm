package org.example.pharm.service;

import jakarta.transaction.Transactional;
import org.example.pharm.model.Role;
import org.example.pharm.model.User;
import org.example.pharm.repository.RoleRepository;
import org.example.pharm.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Возвращает список пользователей, соответствующих ключевому слову.
    public List<User> listAll() {
        return userRepository.findAll(); // Просто возвращаем всех пользователей из репозитория
    }

    // Регистрирует нового пользователя с указанной ролью.
    // Если роль не указана, назначается роль "ROLE_USER" по умолчанию.
    @Transactional
    public User registerUser(User user) {
        // Код для шифрования пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Получаем роль "ROLE_USER"
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(null);
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


    // Ищет пользователя по имени пользователя (логину).
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Возвращает пользователя по его идентификатору.
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден."));
    }

    // Сохраняет пользователя, обновляя его данные (с кодировкой пароля).
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    // Удаляет пользователя по его идентификатору.
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // Метод для обновления роли пользователя на ROLE_ADMIN
    public void assignRoleToAdmin(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Получаем роль ROLE_ADMIN, если она не найдена, выбрасываем исключение
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new IllegalArgumentException("Роль ADMIN не найдена"));

            // Убираем все текущие роли пользователя, если нужно, и добавляем роль ROLE_ADMIN
            user.getRoles().clear(); // Убираем текущие роли, если требуется
            user.getRoles().add(adminRole); // Добавляем роль ROLE_ADMIN

            // Сохраняем обновленного пользователя
            userRepository.save(user);
        }
    }


}


