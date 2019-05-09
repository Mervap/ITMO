package ru.itmo.wm4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wm4.domain.User;
import ru.itmo.wm4.service.UserService;

@Controller
public class UserPage extends Page {
    private final UserService userService;

    public UserPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/user/{id:[\\d]{1,10}}}")
    public String main(Model model, @PathVariable long id) {
        User user = userService.findById(id);

        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("error", "No such user");
        }
        return "UserPage";
    }
}
