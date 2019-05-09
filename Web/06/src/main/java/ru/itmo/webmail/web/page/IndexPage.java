package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.service.ArticleService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class IndexPage extends Page {


    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private void registrationDone(HttpServletRequest request, Map<String, Object> view) {
        view.put("message", "You have been registered");
    }

    private List<ArticleService.ArticleView> findArticles(HttpServletRequest request, Map<String, Object> view) {
        return getArticleService().findNotHidden();
    }
}
