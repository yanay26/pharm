package org.example.pharm.controller;

import org.example.pharm.model.Product;
import org.example.pharm.model.User;
import org.example.pharm.service.UserService;
import org.example.pharm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для обработки запросов, связанных с продуктами и пользователями.
 * Обрабатывает страницы для отображения, добавления, редактирования и удаления продуктов,
 * а также регистрации и авторизации пользователей.
 */
@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    /**
     * Отображает главную страницу с продуктами.
     * Также фильтрует продукты по ключевому слову и отображает их на странице.
     *
     * @param model модель для передачи данных на страницу
     * @param keyword ключевое слово для фильтрации продуктов (необязательно)
     * @return имя шаблона для главной страницы
     */
    @RequestMapping
    public String viewHomePage(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Product> listProduct = productService.listAll(keyword);
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("keyword", keyword);
        return "index"; // Главная страница
    }

    /**
     * Отображает страницу для добавления нового продукта.
     *
     * @param model модель для передачи данных на страницу
     * @return имя шаблона для формы добавления нового продукта
     */
    @RequestMapping("/new")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "new_product"; // Форма для добавления нового продукта
    }

    /**
     * Сохраняет новый продукт.
     *
     * @param product продукт для сохранения
     * @return перенаправление на главную страницу после сохранения
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/"; // Перенаправление на главную страницу
    }

    /**
     * Отображает страницу для редактирования продукта.
     *
     * @param id идентификатор продукта
     * @return объект ModelAndView для отображения страницы редактирования
     */
    @RequestMapping("/edit/{id}")
    public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit_product");
        Product product = productService.get(id);
        mav.addObject("product", product);
        return mav;
    }

    /**
     * Удаляет продукт.
     *
     * @param id идентификатор продукта для удаления
     * @return перенаправление на главную страницу после удаления
     */
    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        productService.delete(id);
        return "redirect:/"; // Перенаправление на главную страницу
    }

    /**
     * Отображает страницу с гистограммой для продуктов.
     *
     * @param model модель для передачи данных на страницу
     * @return имя шаблона для страницы с гистограммой
     */
    @GetMapping("/histogram")
    public String showHistogram(Model model) {
        Map<LocalDate, Long> productsCount = productService.getProductsCountByDeliveryDate();
        model.addAttribute("productsCount", productsCount);
        return "histogram"; // Страница с гистограммой
    }

    /**
     * Отображает страницу регистрации нового пользователя.
     *
     * @param model модель для передачи данных на страницу
     * @return имя шаблона для страницы регистрации
     */
    @RequestMapping("/register")
    public String showRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register"; // Шаблон страницы регистрации
    }

    /**
     * Обрабатывает регистрацию нового пользователя.
     *
     * @param user пользователь для регистрации
     * @return перенаправление на страницу логина после регистрации
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user) {
        userService.registerUser(user);  // Роль по умолчанию будет присвоена внутри UserService
        return "redirect:/login";
    }

    /**
     * Отображает страницу авторизации пользователя.
     *
     * @param model модель для передачи данных на страницу
     * @return имя шаблона для страницы авторизации
     */
    @RequestMapping("/login")
    public String showLoginForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login"; // Шаблон страницы логина
    }

    /**
     * Обрабатывает ошибку авторизации и отображает сообщение при выходе.
     *
     * @param error сообщение об ошибке входа
     * @param logout сообщение о выходе
     * @param model модель для передачи данных на страницу
     * @return имя шаблона для страницы логина
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Неверные имя пользователя или пароль");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Вы успешно вышли из системы");
        }
        return "login";
    }

    /**
     * Отображает страницу пользователей, доступную только администраторам.
     *
     * @param model модель для передачи данных на страницу
     * @return имя шаблона для страницы пользователей
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String viewUsersPage(Model model) {
        List<User> users = userService.listAll(); // Получаем всех пользователей
        model.addAttribute("users", users);
        return "users"; // Имя шаблона Thymeleaf (users.html)
    }

    /**
     * Удаляет пользователя.
     *
     * @param id идентификатор пользователя для удаления
     * @return перенаправление на страницу управления пользователями
     */
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id); // Вызываем метод сервиса для удаления пользователя
        return "redirect:/users"; // Перенаправляем обратно на страницу управления пользователями
    }

    /**
     * Назначает роль администратора пользователю.
     *
     * @param userId идентификатор пользователя
     * @return перенаправление на страницу с пользователями
     */
    @PostMapping("/assignRole/{userId}")
    public String assignRole(@PathVariable Long userId) {
        userService.assignRoleToAdmin(userId); // Обновляем роль пользователя
        return "redirect:/users"; // Перенаправляем на страницу с пользователями
    }

    /**
     * Отображает страницу с информацией о проекте.
     *
     * @return имя шаблона для страницы "Об авторе"
     */
    @GetMapping("/author")
    public String aboutPage() {
        return "author";
    }

}
