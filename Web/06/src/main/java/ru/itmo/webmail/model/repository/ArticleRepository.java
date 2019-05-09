package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Article;

import java.util.Date;
import java.util.List;

public interface ArticleRepository {
    Article find(long articleId);

    List<Article> findNotHidden();

    List<Article> findByUserId(long userId);

    void updateHidden(long articleId, boolean hidden);

    Date getCurrentTime();

    void save(Article article);
}
