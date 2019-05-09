package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.User;

import java.util.List;

public interface UserRepository {
    User find(long userId);

    User findByLogin(String login);

    User findByEmail(String email);

    User findByLoginAndPasswordSha(String login, String passwordSha);

    User findByEmailAndPasswordSha(String login, String passwordSha);

    User findByLoginOrEmailAndPasswordSha(String login, String passwordSha);

    List<User> findAll();

    void updateConfirmation(User user);

    void save(User user, String passwordSha);
}
