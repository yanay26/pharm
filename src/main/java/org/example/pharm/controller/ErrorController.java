package org.example.pharm.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @GetMapping("/403")
    public String handleAccessDenied(Model model) {
        model.addAttribute("errorMessage", "У вас нет прав для доступа к этой странице.");
        return "error/403";
    }

    @GetMapping("/404")
    public String handleNotFound(Model model) {
        model.addAttribute("errorMessage", "Страница не найдена.");
        return "error/404";
    }

    @RequestMapping("/error_general")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode != null) {
            if (statusCode == 404) {
                model.addAttribute("errorMessage", "Страница не найдена.");
                return "error/404";
            } else if (statusCode == 403) {
                model.addAttribute("errorMessage", "У вас нет прав для доступа к этой странице.");
                return "error/403";
            } else if (statusCode == 500) {
                model.addAttribute("errorMessage", "Произошла внутренняя ошибка сервера.");
                return "error/500";
            }
        }
        model.addAttribute("errorMessage", "Произошла неизвестная ошибка.");
        return "error/general";
    }

    @GetMapping("/test500")
    public String test500() {
        // Искусственно вызываем ошибку 500
        throw new RuntimeException("Произошла ошибка на сервере");
    }

    @GetMapping("/testUnknown")
    public String testUnknown() {
        // Искусственно вызываем исключение, которое не перехватывается
        throw new IllegalArgumentException("Неизвестная ошибка");
    }

}


