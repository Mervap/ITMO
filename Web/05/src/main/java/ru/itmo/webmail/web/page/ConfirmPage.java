package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.EmailConfirmation;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ConfirmPage extends Page {

    private void action(HttpServletRequest request, Map<String, Object> view) {
        String secret = request.getParameter("secret");
        EmailConfirmation emailConfirmation = getEmailConfirmationService().findBySecret(secret);

        if (emailConfirmation == null) {
            throw new RedirectException("/index", "emailConfirmationFail");
        } else {
            getUserService().confirmEmail(getUserService().find(emailConfirmation.getUserId()));
            throw new RedirectException("/index", "emailConfirmationDone");
        }
    }
}
