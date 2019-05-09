package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Talk;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.TalkRepository;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.TalkRepositoryImpl;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class TalkService {
    private TalkRepository talkRepository = new TalkRepositoryImpl();
    private UserRepository userRepository = new UserRepositoryImpl();

    public List<TalkView> findAllTalkViews(long userId) {
        List<Talk> talks = talkRepository.findAll(userId);

        List<TalkView> result = new ArrayList<>();
        for (Talk talk : talks) {
            TalkView talkView = new TalkView();
            talkView.setSourceUser(userRepository.find(talk.getSourceUserId()));
            talkView.setTargetUser(userRepository.find(talk.getTargetUserId()));
            talkView.setText(talk.getText());
            result.add(talkView);
        }

        return result;
    }

    public void validateTalk(String targetUserLogin, String text) throws ValidationException {
        if (targetUserLogin == null || targetUserLogin.isEmpty()) {
            throw new ValidationException("Login of recipient is required");
        }

        if (text == null || text.isEmpty()) {
            throw new ValidationException("Text of message is required");
        }

        if (userRepository.findByLogin(targetUserLogin) == null) {
            throw new ValidationException("Invalid login of recipient");
        }
    }

    public void send(Talk talk) {
        talkRepository.save(talk);
    }

    public static class TalkView {
        private User sourceUser;
        private User targetUser;
        private String text;

        public User getSourceUser() {
            return sourceUser;
        }

        public void setSourceUser(User sourceUser) {
            this.sourceUser = sourceUser;
        }

        public User getTargetUser() {
            return targetUser;
        }

        public void setTargetUser(User targetUser) {
            this.targetUser = targetUser;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
