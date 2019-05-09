package ru.itmo.webmail.model.repository.impl;

import ru.itmo.webmail.model.domain.News;
import ru.itmo.webmail.model.repository.NewsRepository;
import ru.itmo.webmail.model.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NewsRepositoryImpl implements NewsRepository {

    private static final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    private static final String NEWS_FILE = "News";

    private List <News> news;

    public NewsRepositoryImpl() {
        try {
            news = (List <News>) new ObjectInputStream(
                    new FileInputStream(new File(tmpDir, getClass().getSimpleName() + NEWS_FILE))).readObject();
        } catch (Exception e) {
            news = new ArrayList <>();
        }
    }

    //Task 6
    @Override
    public void saveNews(long userId, String text) {
        News currentNews = new News();
        currentNews.setUserId(userId);
        currentNews.setText(text);
        news.add(currentNews);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(new File(tmpDir, getClass().getSimpleName() + NEWS_FILE)));
            objectOutputStream.writeObject(news);
            objectOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Can't save news.", e);
        }
    }

    //Task 6
    @Override
    public List <News> findAllNews() {
        return new ArrayList<>(news);
    }
}
