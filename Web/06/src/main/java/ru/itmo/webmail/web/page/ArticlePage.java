package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.Article;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage extends Page {

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);

        if (getUser() == null) {
            throw new RedirectException("/index");
        }
    }

    private Map<String, Object> addArticle(HttpServletRequest request, Map<String, Object> view) {
        String title = request.getParameter("title");
        String text = request.getParameter("text");

        try {
            getArticleService().validateArticle(title, text);
        } catch (ValidationException e) {
            view.put("success", false);
            view.put("error", e.getMessage());
            return view;
        }

        Article article = new Article();
        article.setUserId(getUser().getId());
        article.setTitle(title);
        article.setText(text);

        getArticleService().addArticle(article);
        view.put("success", true);

        return view;
    }

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }
}
