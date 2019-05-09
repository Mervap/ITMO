package ru.itmo.webmail.model.repository;

import javafx.util.Pair;
import ru.itmo.webmail.model.domain.User;

import java.util.List;

public interface UserRepository {
    void saveUser(User user);
    User findByLogin(String login);
    User findByEmail(String email);
    User findById(long Id);
    List<User> findAllUsers();
    long findCount();
}
