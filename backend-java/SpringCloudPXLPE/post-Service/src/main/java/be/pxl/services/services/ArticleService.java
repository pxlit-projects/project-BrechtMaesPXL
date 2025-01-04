package be.pxl.services.services;

import be.pxl.services.domain.Article;
import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.domain.dto.ArticleResponse;
import be.pxl.services.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ArticleService implements IArticleService{

    private  final ArticleRepository articleRepository;
    @Override
    public List<ArticleResponse> getAllArticles() {
         List<Article> articleq = articleRepository.findAll();
        return articleq.stream().map(this::mapToArticleResponse).toList();
    }

    private ArticleResponse mapToArticleResponse(Article article) {
        return ArticleResponse.builder()
                .editorsId(article.getEditorsId())
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }
    @Override
    public void addArticle(ArticleRequest articleRequest) {
        Article article = Article.builder()
                .editorsId(articleRequest.getEditorsId())
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .build();
        articleRepository.save(article);
    }
}
