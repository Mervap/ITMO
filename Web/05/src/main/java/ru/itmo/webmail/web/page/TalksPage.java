package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.Talk;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TalksPage extends Page {

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);

        if (getUser() == null) {
            throw new RedirectException("/index");
        }

        view.put("talks", getTalkService().findAllTalkViews(getUser().getId()));
    }

    private void addTalk(HttpServletRequest request, Map<String, Object> view) {
        String targetUserLogin = request.getParameter("targetUserLogin");
        String text = request.getParameter("text");

        try {
            getTalkService().validateTalk(targetUserLogin, text);
        } catch (ValidationException e) {
            view.put("targetUserLogin", targetUserLogin);
            view.put("text", text);
            view.put("error", e.getMessage());
            return;
        }

        Talk talk = new Talk();
        talk.setSourceUserId(getUser().getId());
        talk.setTargetUserId(getUserService().findByLogin(targetUserLogin).getId());
        talk.setText(text);

        getTalkService().send(talk);
        throw new RedirectException("/talks");
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        //No operations
    }
}
