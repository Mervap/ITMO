package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.User;

import java.util.List;

public interface UserRepository {
    User find(long userId);

    User findByLogin(String login);

    User findByLoginAndPasswordSha(String login, String passwordSha);

    void updateAdmin(long userId, boolean admin);

    List<User> findAllWithoutAdmin();

    List<User> findAll();

    void save(User user, String passwordSha);
}
