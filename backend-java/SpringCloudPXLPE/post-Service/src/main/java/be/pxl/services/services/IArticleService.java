package be.pxl.services.services;

import be.pxl.services.domain.Article;
import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.domain.dto.ArticleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IArticleService {

    List<ArticleResponse> getAllArticles();

    void addArticle(ArticleRequest article);
}
