package org.example.pharm.service;

import jakarta.transaction.Transactional;
import org.example.pharm.model.Role;
import org.example.pharm.model.User;
import org.example.pharm.repository.RoleRepository;
import org.example.pharm.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервисный класс для работы с пользователями и их ролями.
 * Обеспечивает регистрацию, управление пользователями, их ролями и взаимодействие с репозиториями.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param userRepository репозиторий для работы с пользователями
     * @param roleRepository репозиторий для работы с ролями
     * @param passwordEncoder кодировщик паролей
     */
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return список всех пользователей
     */
    public List<User> listAll() {
        return userRepository.findAll(); // Просто возвращаем всех пользователей из репозитория
    }

    /**
     * Регистрирует нового пользователя с ролью "ROLE_USER".
     * Если роль "ROLE_USER" не существует, она создается.
     * Пароль пользователя шифруется перед сохранением.
     *
     * @param user объект пользователя, который необходимо зарегистрировать
     * @return зарегистрированный пользователь
     */
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

    /**
     * Ищет пользователя по имени пользователя (логину).
     *
     * @param username имя пользователя для поиска
     * @return найденный пользователь или null, если не найден
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь
     * @throws RuntimeException если пользователь не найден
     */
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден."));
    }

    /**
     * Сохраняет или обновляет пользователя, шифруя его пароль перед сохранением.
     *
     * @param user объект пользователя, который необходимо сохранить
     * @return сохраненный или обновленный пользователь
     */
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Обновляет роль пользователя, назначая ему роль "ROLE_ADMIN".
     * Удаляет все текущие роли пользователя и присваивает только роль "ROLE_ADMIN".
     *
     * @param userId идентификатор пользователя, которому нужно назначить роль администратора
     * @throws IllegalArgumentException если роль "ROLE_ADMIN" не найдена
     */
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



