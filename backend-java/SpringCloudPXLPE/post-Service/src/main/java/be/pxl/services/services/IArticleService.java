package be.pxl.services.services;

import be.pxl.services.domain.Article;
import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.domain.dto.ArticleResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface IArticleService {

    List<ArticleResponse> getAllArticles();
    List<ArticleResponse> getAllArticlesByStatus(String status);
    void addArticle(ArticleRequest article);

    void updateArticle(Long id, ArticleRequest article);
    void changeStatus(Long id, String status);
    List<ArticleResponse> getArticlesWithFilter(String content, Long editorsId, LocalDate date);

    void checkIfRoleIsEditor(String role);
}
