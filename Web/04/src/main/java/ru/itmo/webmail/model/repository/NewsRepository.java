package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.News;

import java.util.List;

public interface NewsRepository {
    void saveNews(long userId, String text);
    List<News> findAllNews();

    class NewsView {
        private String login;
        private String text;

        public void setText(String text) {
            this.text = text;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getText() {
            return text;
        }

        public String getLogin() {
            return login;
        }
    }
}
