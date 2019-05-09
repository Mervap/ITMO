package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MyArticlesPage extends Page {

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);

        if (getUser() == null) {
            throw new RedirectException("/index");
        }
    }

    private Map<String, Object> hideShowArticle(HttpServletRequest request, Map<String, Object> view) {
        long articleId = Long.valueOf(request.getParameter("articleId"));
        boolean hidden = Boolean.valueOf(request.getParameter("hidden"));

        try {
            getArticleService().validateArticleUpdate(getUser().getId(), articleId, hidden);
        } catch (ValidationException e) {
            view.put("success", false);
            view.put("error", e.getMessage());
            return view;
        }

        getArticleService().updateHidden(articleId, hidden);
        view.put("success", true);

        return view;
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("userArticles", getArticleService().findByUserId(getUser().getId()));
    }
}
