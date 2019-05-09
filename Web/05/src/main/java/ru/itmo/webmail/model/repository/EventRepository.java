package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Event;

public interface EventRepository {
    void save(Event event);
}
