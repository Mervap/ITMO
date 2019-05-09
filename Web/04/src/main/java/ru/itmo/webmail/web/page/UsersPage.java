package ru.itmo.webmail.web.page;

import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class UsersPage extends Page {
    private UserRepository userRepository = new UserRepositoryImpl();

    private void action(Map <String, Object> view) {
        view.put("users", userRepository.findAllUsers());
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
