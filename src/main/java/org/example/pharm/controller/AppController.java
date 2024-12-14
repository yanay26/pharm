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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Главная страница с продуктами
    @RequestMapping
    public String viewHomePage(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Product> listProduct = productService.listAll(keyword);
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("keyword", keyword);

        // Получаем количество продуктов по дате поставки
        Map<LocalDate, Long> productsByDeliveryDateMap = productService.getProductsCountByDeliveryDate();
        List<Map.Entry<LocalDate, Long>> productsByDeliveryDateList = new ArrayList<>(productsByDeliveryDateMap.entrySet());
        model.addAttribute("productsByDeliveryDateList", productsByDeliveryDateList);

        // Средняя цена
        BigDecimal averagePrice = productService.getAveragePrice();
        model.addAttribute("averagePrice", averagePrice);

        return "index"; // Главная страница
    }

    // Страница для добавления нового продукта
    @RequestMapping("/new")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "new_product"; // Форма для добавления нового продукта
    }

    // Сохранение нового продукта
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/"; // Перенаправление на главную страницу
    }

    // Страница редактирования продукта
    @RequestMapping("/edit/{id}")
    public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit_product");
        Product product = productService.get(id);
        mav.addObject("product", product);
        return mav;
    }

    // Удаление продукта
    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        productService.delete(id);
        return "redirect:/"; // Перенаправление на главную страницу
    }

    // Страница с гистограммой
    @GetMapping("/histogram")
    public String showHistogram(Model model) {
        Map<LocalDate, Long> productsCount = productService.getProductsCountByDeliveryDate();
        model.addAttribute("productsCount", productsCount);
        return "histogram"; // Страница с гистограммой
    }


    // Страница регистрации нового пользователя
    @RequestMapping("/register")
    public String showRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register"; // Шаблон страницы регистрации
    }

    // Обработка регистрации нового пользователя
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user) {
        userService.registerUser(user);  // Роль по умолчанию будет присвоена внутри UserService
        return "redirect:/login";
    }


    // Страница авторизации
    @RequestMapping("/login")
    public String showLoginForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login"; // Шаблон страницы логина
    }

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

    // Маршрут для страницы пользователей, доступный только администраторам
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String viewUsersPage(Model model) {
        List<User> users = userService.listAll(); // Получаем всех пользователей
        model.addAttribute("users", users);
        return "users"; // Имя шаблона Thymeleaf (users.html)
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id); // Вызываем метод сервиса для удаления пользователя
        return "redirect:/users"; // Перенаправляем обратно на страницу управления пользователями
    }

    @PostMapping("/assignRole/{userId}")
    public String assignRole(@PathVariable Long userId) {
        userService.assignRoleToAdmin(userId); // Обновляем роль пользователя
        return "redirect:/users"; // Перенаправляем на страницу с пользователями
    }

    @GetMapping("/author")
    public String aboutPage() {
        return "author";
    }

}




