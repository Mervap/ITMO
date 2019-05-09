package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.EmailConfirmation;

public interface EmailConfirmationRepository {
    EmailConfirmation findBySecret(String secret);

    void save(EmailConfirmation emailConfirmation);
}
