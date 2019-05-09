package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.service.UserService;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//Task 5
public class EnterPage extends Page {
    private UserService userService = new UserService();

    private void enter(HttpServletRequest request, Map <String, Object> view) {
        User user;
        String loginOrEmail = request.getParameter("loginOrEmail");
        String password = request.getParameter("password");

        try {
            user = userService.validateEnter(loginOrEmail, password);
        } catch (ValidationException e) {
            view.put("loginOrEmail", loginOrEmail);
            view.put("password", password);
            view.put("error", e.getMessage());
            return;
        }

        request.getSession().setAttribute("currentUser", user);
        throw new RedirectException("/index", "enterDone");
    }

    private void action() {
        // No operations.
    }

    @Override
    void before(HttpServletRequest request, Map <String, Object> view) {
        super.before(request, view);
    }

    @Override
    void after(HttpServletRequest request, Map <String, Object> view) {
        super.after(request, view);
    }
}
