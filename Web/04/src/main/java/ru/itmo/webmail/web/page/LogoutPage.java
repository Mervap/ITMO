package ru.itmo.webmail.web.page;

import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//Task 5
public class LogoutPage extends Page {

    private void action(HttpServletRequest request) {
        request.getSession().removeAttribute("currentUser");
        throw new RedirectException("/index", "logOutDone");
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
