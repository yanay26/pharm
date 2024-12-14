package org.example.pharm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для обработки ошибок и отображения соответствующих страниц ошибок.
 * Этот контроллер предоставляет обработку ошибок 403 (Forbidden) и 404 (Not Found),
 * а также возвращает соответствующие страницы с сообщениями об ошибках.
 */
@Controller
public class ErrorController {

    /**
     * Обработка ошибки доступа (HTTP 403).
     * При попытке доступа к защищенной странице без достаточных прав.
     *
     * @param model Модель, в которой будет передано сообщение об ошибке
     * @return Название страницы ошибки (error/403)
     */
    @GetMapping("/403")
    public String handleAccessDenied(Model model) {
        model.addAttribute("errorMessage", "У вас нет прав для доступа к этой странице.");
        return "error/403";
    }

    /**
     * Обработка ошибки не найденной страницы (HTTP 404).
     * При попытке доступа к несуществующей странице.
     *
     * @param model Модель, в которой будет передано сообщение об ошибке
     * @return Название страницы ошибки (error/404)
     */
    @GetMapping("/404")
    public String handleNotFound(Model model) {
        model.addAttribute("errorMessage", "Страница не найдена.");
        return "error/404";
    }
}



