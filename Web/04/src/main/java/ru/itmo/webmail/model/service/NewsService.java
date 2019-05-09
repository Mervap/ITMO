package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.News;
import ru.itmo.webmail.model.repository.NewsRepository;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.NewsRepositoryImpl;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class NewsService {
    private NewsRepository newsRepository = new NewsRepositoryImpl();
    private UserRepository userRepository = new UserRepositoryImpl();

    //Task 6
    public List <NewsRepository.NewsView> findNewsViews() {
        List <NewsRepository.NewsView> result = new ArrayList <>();
        for (News news : newsRepository.findAllNews()) {
            NewsRepository.NewsView newsView = new NewsRepository.NewsView();
            newsView.setLogin(userRepository.findById(news.getUserId()).getLogin());
            newsView.setText(news.getText());
            result.add(newsView);
        }
        return result;
    }
}
