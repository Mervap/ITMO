package ru.itmo.webmail.model.repository.impl;

import javafx.util.Pair;
import ru.itmo.webmail.model.domain.News;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private static final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    private static final String USERS_FILE = "Users";

    private List <User> users;

    public UserRepositoryImpl() {
        try {
            //noinspection unchecked
            users = (List <User>) new ObjectInputStream(
                    new FileInputStream(new File(tmpDir, getClass().getSimpleName() + USERS_FILE))).readObject();
        } catch (Exception e) {
            users = new ArrayList <>();
        }
    }

    @Override
    public void saveUser(User user) {
        //Task 2
        user.setId(users.size() + 1);
        users.add(user);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(new File(tmpDir, getClass().getSimpleName() + USERS_FILE)));
            objectOutputStream.writeObject(users);
            objectOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Can't save user.", e);
        }
    }

    @Override
    public User findByLogin(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst().orElse(null);
    }


    @Override
    public User findByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public User findById(long id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    //Task 2
    @Override
    public List <User> findAllUsers() {
        return new ArrayList <>(users);
    }

    //Task 3
    @Override
    public long findCount() {
        return users.size();
    }
}
