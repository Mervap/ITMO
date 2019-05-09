package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Event;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.EventRepository;
import ru.itmo.webmail.model.repository.impl.EventRepositoryImpl;

public class EventService {
    private EventRepository eventRepository = new EventRepositoryImpl();

    public void onEnter(User user) {
        Event event = new Event();
        event.setUserId(user.getId());
        event.setType(Event.Type.ENTER);
        eventRepository.save(event);
    }

    public void onLogout(User user) {
        Event event = new Event();
        event.setUserId(user.getId());
        event.setType(Event.Type.LOGOUT);
        eventRepository.save(event);
    }
}
