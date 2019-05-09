package ru.itmo.webmail.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class IndexPage extends Page {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private void registrationDone(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "You have been registered");
    }

    private void emailConfirmationDone(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "Email successfully confirmed");
    }

    private void emailConfirmationFail(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "Email confirmation failed");
    }
}
