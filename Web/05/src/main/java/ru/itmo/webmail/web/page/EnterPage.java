package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class EnterPage extends Page {
    private void enter(HttpServletRequest request, Map<String, Object> view) {
        String loginOrEmail = request.getParameter("loginOrEmail");
        String password = request.getParameter("password");

        try {
            getUserService().validateEnter(loginOrEmail, password);
        } catch (ValidationException e) {
            view.put("loginOrEmail", loginOrEmail);
            view.put("password", password);
            view.put("error", e.getMessage());
            return;
        }

        User user = getUserService().authorize(loginOrEmail, password);
        request.getSession(true).setAttribute(USER_ID_SESSION_KEY, user.getId());

        getEventService().onEnter(user);
        throw new RedirectException("/index");
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }
}
