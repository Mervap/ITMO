package ru.itmo.webmail.model.service;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

public class UserService {
    private static final String USER_PASSWORD_SALT = "dc3475f2b301851b";

    private UserRepository userRepository = new UserRepositoryImpl();

    public void validateRegistration(User user, String password, String passwordConfirmation, String email) throws ValidationException {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Login is required");
        }
        if (!user.getLogin().matches("[a-z]+")) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (user.getLogin().length() > 8) {
            throw new ValidationException("Login can't be longer than 8");
        }
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new ValidationException("Login is already in use");
        }

        //Task 2
        if (email == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Email is required");
        }

        if (StringUtils.countMatches(email, "@") != 1) {
            throw new ValidationException("Email can contain only one '@'");
        }

        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4");
        }
        if (password.length() > 32) {
            throw new ValidationException("Password can't be longer than 32");
        }

        if (!password.equals(passwordConfirmation)) {
            throw new ValidationException("Password and password confirmation not equal");
        }
    }

    //Task 5
    public User validateEnter(String loginOrEmail, String password) throws ValidationException {
        if (loginOrEmail == null || loginOrEmail.isEmpty()) {
            throw new ValidationException("Login/email is required");
        }

        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password is required");
        }

        User currentUser = userRepository.findByLogin(loginOrEmail);
        if (currentUser == null) {
            currentUser = userRepository.findByEmail(loginOrEmail);
        }

        if (currentUser == null || !Hashing.sha256().hashString(USER_PASSWORD_SALT + password,
                StandardCharsets.UTF_8).toString().equals(currentUser.getPasswordSha1())) {
            throw new ValidationException("Incorrect login/email or password");
        }

        return currentUser;
    }

    public void register(User user, String password) {
        user.setPasswordSha1(Hashing.sha256().hashString(USER_PASSWORD_SALT + password,
                StandardCharsets.UTF_8).toString());
        userRepository.saveUser(user);
    }

}
