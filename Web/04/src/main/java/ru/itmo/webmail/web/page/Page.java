package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.NewsRepository;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.NewsRepositoryImpl;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;
import ru.itmo.webmail.model.service.NewsService;
import ru.itmo.webmail.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//Task 4
public class Page {

    private UserRepository userRepository = new UserRepositoryImpl();
    private NewsService newsService = new NewsService();

    void before(HttpServletRequest request, Map <String, Object> view) {
    }

    void after(HttpServletRequest request, Map <String, Object> view) {
        view.put("userCount", userRepository.findCount());

        //Task 5
        User user = (User) request.getSession().getAttribute("currentUser");
        if (user != null) {
            view.put("logInUser", user.getLogin());
        }

        //Task 6
        view.put("news", newsService.findNewsViews());
    }
}
