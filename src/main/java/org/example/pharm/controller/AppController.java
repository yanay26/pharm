package org.example.pharm.controller;

import org.example.pharm.model.User;
import org.example.pharm.service.CategoryService;
import org.example.pharm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Контроллер, обрабатывающий запросы, связанные с отображением страниц и операциями с пользователями и категориями.
 * <p>
 * Этот контроллер обрабатывает все основные страницы приложения, включая главную, страницы для редактирования продуктов,
 * регистрации и авторизации, а также страницу для управления пользователями (доступно только администратору).
 */
@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    /**
     * Обрабатывает запрос на главную страницу.
     * @param model модель для передачи данных в представление
     * @return имя шаблона для отображения главной страницы
     */
    @GetMapping
    public String viewHomePage(Model model) {
        return "index";
    }

    /**
     * Отображает страницу для редактирования товара.
     * @param id идентификатор продукта, который необходимо отредактировать
     * @return имя шаблона для редактирования продукта
     */
    @GetMapping("/edit-product/{id}")
    public String editProduct(@PathVariable Long id) {
        return "edit_product";
    }

    /**
     * Отображает страницу для добавления нового продукта.
     * @return имя шаблона для добавления нового продукта
     */
    @GetMapping("/new-product")
    public String newProduct() {
        return "new_product";
    }

    /**
     * Отображает страницу со списком всех категорий.
     * @param model модель для передачи списка категорий в представление
     * @return имя шаблона для отображения категорий
     */
    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    /**
     * Отображает страницу с гистограммой.
     * @param model модель для передачи данных в представление
     * @return имя шаблона для отображения гистограммы
     */
    @GetMapping("/histogram")
    public String showHistogram(Model model) {
        return "histogram";
    }

    /**
     * Отображает страницу "Об авторе".
     * @return имя шаблона для отображения информации об авторе
     */
    @GetMapping("/author")
    public String aboutPage() {
        return "author";
    }

    /**
     * Отображает форму регистрации нового пользователя.
     * @param model модель для передачи объекта пользователя в форму
     * @return имя шаблона для страницы регистрации
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    /**
     * Обрабатывает запрос на регистрацию нового пользователя.
     * @param user объект пользователя, данные которого были введены в форму регистрации
     * @return редирект на страницу логина после успешной регистрации
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("emailError", e.getMessage());
            return "register";
        }
    }

    /**
     * Отображает форму для авторизации пользователя.
     * @param error сообщение об ошибке при входе (если есть)
     * @param logout сообщение о выходе из системы (если есть)
     * @param model модель для передачи данных в форму
     * @return имя шаблона для страницы логина
     */
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        User user = new User();
        model.addAttribute("user", user);

        if (error != null) {
            model.addAttribute("errorMessage", "Неверные имя пользователя или пароль");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Вы успешно вышли из системы");
        }

        return "login";
    }

    /**
     * Отображает страницу для управления пользователями, доступную только для администратора.
     * @param model модель для передачи данных о пользователях
     * @return имя шаблона для отображения страницы с пользователями
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String viewUsersPage(Model model) {
        List<User> users = userService.listAll(); // Получаем всех пользователей
        model.addAttribute("users", users);
        return "users";
    }

    /**
     * Удаляет пользователя по заданному идентификатору, доступно только для администратора.
     * @param id идентификатор пользователя, которого нужно удалить
     * @return редирект на страницу управления пользователями
     */
    @PostMapping("/users/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id); // Вызываем метод сервиса для удаления пользователя
        return "redirect:/users";
    }

    /**
     * Назначает роль администратора пользователю по его идентификатору, доступно только для администратора.
     * @param userId идентификатор пользователя, которому нужно назначить роль администратора
     * @return редирект на страницу управления пользователями
     */
    @PostMapping("/assignRole/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignRole(@PathVariable Long userId) {
        userService.assignRoleToAdmin(userId); // Обновляем роль пользователя
        return "redirect:/users";
    }
}
