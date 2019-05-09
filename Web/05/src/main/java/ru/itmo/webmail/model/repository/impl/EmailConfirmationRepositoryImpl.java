package ru.itmo.webmail.model.repository.impl;

import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.domain.EmailConfirmation;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.repository.EmailConfirmationRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;

public class EmailConfirmationRepositoryImpl implements EmailConfirmationRepository {
    private static final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public EmailConfirmation findBySecret(String secret) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM EmailConfirmation WHERE secret=?")) {
                statement.setString(1, secret);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return toEmailConfirmation(statement.getMetaData(), resultSet);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User by secret.", e);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private EmailConfirmation toEmailConfirmation(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        EmailConfirmation emailConfirmation = new EmailConfirmation();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            if ("id".equalsIgnoreCase(columnName)) {
                emailConfirmation.setId(resultSet.getLong(i));
            } else if ("userId".equalsIgnoreCase(columnName)) {
                emailConfirmation.setUserId(resultSet.getLong(i));
            } else if ("secret".equalsIgnoreCase(columnName)) {
                emailConfirmation.setSecret(resultSet.getString(i));
            } else if ("creationTime".equalsIgnoreCase(columnName)) {
                emailConfirmation.setCreationTime(resultSet.getTimestamp(i));
            } else {
                throw new RepositoryException("Unexpected column 'EmailConfirmation." + columnName + "'.");
            }
        }
        return emailConfirmation;
    }

    @Override
    public void save(EmailConfirmation emailConfirmation) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO EmailConfirmation (userId, secret, creationTime) VALUES (?, ?, NOW())",
                    Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, emailConfirmation.getUserId());
                statement.setString(2, emailConfirmation.getSecret());
                if (statement.executeUpdate() == 1) {
                    ResultSet generatedIdResultSet = statement.getGeneratedKeys();
                    if (generatedIdResultSet.next()) {
                        emailConfirmation.setId(generatedIdResultSet.getLong(1));
                        emailConfirmation.setCreationTime(findCreationTime(emailConfirmation.getId()));
                    } else {
                        throw new RepositoryException("Can't find id of saved EmailConfirmation.");
                    }
                } else {
                    throw new RepositoryException("Can't save EmailConfirmation.");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save secret of email confirmation.", e);
        }
    }

    private Date findCreationTime(long emailConfirmationId) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT creationTime FROM EmailConfirmation WHERE id=?")) {
                statement.setLong(1, emailConfirmationId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getTimestamp(1);
                    }
                }
                throw new RepositoryException("Can't find EmailConfirmation.creationTime by id.");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find EmailConfirmation.creationTime by id.", e);
        }
    }
}
