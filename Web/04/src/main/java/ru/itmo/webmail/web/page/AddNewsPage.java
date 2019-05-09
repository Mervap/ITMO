package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.NewsRepository;
import ru.itmo.webmail.model.repository.impl.NewsRepositoryImpl;
import ru.itmo.webmail.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//Task 6
public class AddNewsPage extends Page {
    private NewsRepository NewsRepository = new NewsRepositoryImpl();

    private void action() {
        // No operations.
    }

    private void addNews(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("currentUser");
        NewsRepository.saveNews(user.getId(), request.getParameter("text"));
        throw new RedirectException("/index", "addNewsDone");
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
