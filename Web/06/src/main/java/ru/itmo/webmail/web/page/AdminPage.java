package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class AdminPage extends Page {

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);

        if (getUser() == null) {
            throw new RedirectException("/index");
        }
    }

    private Map<String, Object> changeAdmin(HttpServletRequest request, Map<String, Object> view) {
        long userId = Long.valueOf(request.getParameter("userId"));
        boolean admin = Boolean.valueOf(request.getParameter("admin"));

        try {
            getUserService().validateAdminUpdate(getUser().getId(), userId, admin);
        } catch (ValidationException e) {
            view.put("success", false);
            view.put("error", e.getMessage());
            return view;
        }

        getUserService().updateAdmin(userId, admin);
        view.put("success", true);

        return view;
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        List<User> users;
        if (getUser().isAdmin()) {
            users = getUserService().findAll();
            view.put("admin", true);
        } else {
            users = getUserService().findAllWithAdmin();
        }

        view.put("users", users);
    }
}
