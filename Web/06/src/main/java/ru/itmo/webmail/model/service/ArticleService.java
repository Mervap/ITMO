package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Article;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.ArticleRepository;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.ArticleRepositoryImpl;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class ArticleService {
    private static final String USER_PASSWORD_SALT = "dc3475f2b301851b";

    private ArticleRepository articleRepository = new ArticleRepositoryImpl();
    private UserRepository userRepository = new UserRepositoryImpl();

    public void validateArticle(String title, String text) throws ValidationException {
        if (title == null || title.isEmpty()) {
            throw new ValidationException("Title is required");
        }

        if (text == null || text.isEmpty()) {
            throw new ValidationException("Text of message is required");
        }
    }

    public void validateArticleUpdate(long userId, long articleId, boolean hidden) throws ValidationException {
        Article article = articleRepository.find(articleId);
        if (article.getUserId() != userId) {
            throw new ValidationException("You haven't permissions to change this Article");
        }

        if (article.isHidden() == hidden) {
            throw new ValidationException("Article already Hide/Show");
        }
    }

    public void updateHidden(long articleId, boolean hidden) {
        articleRepository.updateHidden(articleId, hidden);
    }

    public List<ArticleView> findNotHidden() {
        List<Article> articles = articleRepository.findNotHidden();
        Date currentTime = articleRepository.getCurrentTime();

        List<ArticleView> result = new ArrayList<>();
        for (Article article : articles) {
            ArticleView articleView = new ArticleView();
            articleView.setUser(userRepository.find(article.getUserId()));
            articleView.setTitle(article.getTitle());
            articleView.setText(article.getText());
            articleView.setTimeAgo(getDifference(article.getCreationTime(), currentTime));

            result.add(articleView);
        }

        return result;
    }

    public List<Article> findByUserId(long userId) {
        return articleRepository.findByUserId(userId);
    }

    public void addArticle(Article article) {
        articleRepository.save(article);
    }

    private String getDifference(Date firstDate, Date secondDate) {
        if (firstDate.after(secondDate)) {
            Date tmp = firstDate;
            firstDate = secondDate;
            secondDate = tmp;
        }

        Calendar begin = new GregorianCalendar();
        begin.setTimeInMillis(0);

        Calendar differenceOfDates = new GregorianCalendar();
        differenceOfDates.setTimeInMillis(secondDate.getTime() - firstDate.getTime());

        if (begin.get(Calendar.YEAR) != differenceOfDates.get(Calendar.YEAR)) {
            int difference = differenceOfDates.get(Calendar.YEAR) - begin.get(Calendar.YEAR);
            return String.valueOf(difference) + " years ago";
        }

        if (begin.get(Calendar.MONTH) != differenceOfDates.get(Calendar.MONTH)) {
            int difference = differenceOfDates.get(Calendar.MONTH) - begin.get(Calendar.MONTH);
            return String.valueOf(difference) + " months ago";
        }

        if (begin.get(Calendar.DAY_OF_MONTH) != differenceOfDates.get(Calendar.DAY_OF_MONTH)) {
            int difference = differenceOfDates.get(Calendar.DAY_OF_MONTH) - begin.get(Calendar.DAY_OF_MONTH);
            return String.valueOf(difference) + " days ago";
        }

        if (begin.get(Calendar.HOUR_OF_DAY) != differenceOfDates.get(Calendar.HOUR_OF_DAY)) {
            int difference = differenceOfDates.get(Calendar.HOUR_OF_DAY) - begin.get(Calendar.HOUR_OF_DAY);
            return String.valueOf(difference) + " hours ago";
        }

        if (begin.get(Calendar.MINUTE) != differenceOfDates.get(Calendar.MINUTE)) {
            int difference = differenceOfDates.get(Calendar.MINUTE) - begin.get(Calendar.MINUTE);
            return String.valueOf(difference) + " minutes ago";
        }

        return "at this moment";
    }

    public static class ArticleView {
        private User user;
        private String title;
        private String text;
        private String timeAgo;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTimeAgo() {
            return timeAgo;
        }

        public void setTimeAgo(String timeAgo) {
            this.timeAgo = timeAgo;
        }
    }
}
