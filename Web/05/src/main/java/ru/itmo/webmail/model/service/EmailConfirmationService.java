package ru.itmo.webmail.model.service;

import com.google.common.hash.Hashing;
import ru.itmo.webmail.model.domain.EmailConfirmation;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.EmailConfirmationRepository;
import ru.itmo.webmail.model.repository.impl.EmailConfirmationRepositoryImpl;

import java.nio.charset.StandardCharsets;

public class EmailConfirmationService {
    private static final String USER_SECRET_SALT = "sjk5795rdf283r124sd093";

    private EmailConfirmationRepository emailConfirmationRepository = new EmailConfirmationRepositoryImpl();

    public EmailConfirmation findBySecret(String secret) {
        return emailConfirmationRepository.findBySecret(secret);
    }

    public void sendConfirmation(User user) {
        EmailConfirmation emailConfirmation = new EmailConfirmation();
        emailConfirmation.setUserId(user.getId());
        emailConfirmation.setSecret(Hashing.sha256().hashString(USER_SECRET_SALT + user.getLogin(), StandardCharsets.UTF_8).toString());
        emailConfirmationRepository.save(emailConfirmation);
    }
}
