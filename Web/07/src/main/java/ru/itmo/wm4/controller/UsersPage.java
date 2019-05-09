package ru.itmo.wm4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wm4.form.UserDisableCredentials;
import ru.itmo.wm4.form.validator.UserDisableCredentialsValidator;
import ru.itmo.wm4.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UsersPage extends Page {
    private final UserService userService;
    private final UserDisableCredentialsValidator userCredentialsDisableEnableValidator;

    public UsersPage(UserService userService, UserDisableCredentialsValidator userCredentialsDisableEnableValidator) {
        this.userService = userService;
        this.userCredentialsDisableEnableValidator = userCredentialsDisableEnableValidator;
    }

    @InitBinder("disableEnableForm")
    public void initDisableEnableFormBinder(WebDataBinder binder) {
        binder.addValidators(userCredentialsDisableEnableValidator);
    }

    @GetMapping(path = "/users")
    public String main(Model model, HttpSession httpSession) {
        if (getUser(httpSession) == null) {
            return "redirect:/";
        }

        model.addAttribute("disableEnableForm", new UserDisableCredentials());
        model.addAttribute("users", userService.findAll());
        return "UsersPage";
    }

    @PostMapping(path = "/disableEnableUser")
    public String registerPost(@Valid @ModelAttribute("disableEnableForm") UserDisableCredentials disableEnableForm,
                               BindingResult bindingResult, Model model, HttpSession httpSession) {
        if (bindingResult.hasErrors() || disableEnableForm.getSourceId() != getUser(httpSession).getId()) {
            model.addAttribute("users", userService.findAll());
            return "UsersPage";
        }

        userService.updateDisable(disableEnableForm.getTargetId(), disableEnableForm.isDisable());
        return "redirect:/users";
    }
}
