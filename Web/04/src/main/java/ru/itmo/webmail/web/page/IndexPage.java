package ru.itmo.webmail.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class IndexPage extends Page {
    private void action() {
        // No operations.
    }

    private void registrationDone(Map <String, Object> view) {
        view.put("message", "You have been registered");
    }

    //Task 5
    private void enterDone(Map <String, Object> view) {
        view.put("message", "You have been logged in");
    }

    //Task 5
    private void logOutDone(Map <String, Object> view) {
        view.put("message", "You have been logged out");
    }

    private void addNewsDone(Map <String, Object> view) {
        view.put("message", "News was added");
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
