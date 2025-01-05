package be.pxl.services.services;

import be.pxl.services.Client.ReviewClient;
import be.pxl.services.domain.Article;
import be.pxl.services.domain.StatusArticle;
import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.domain.dto.ArticleResponse;
import be.pxl.services.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ArticleService implements IArticleService{

    private  final ArticleRepository articleRepository;
    private final ReviewClient reviewClient;



    @Override
    public List<ArticleResponse> getAllArticles() {
         List<Article> articleq = articleRepository.findAll();
        return articleq.stream().map(this::mapToArticleResponse).toList();
    }

    @Override
    public void addArticle(ArticleRequest articleRequest) {
        Article article = Article.builder()
                .editorsId(articleRequest.getEditorsId())
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .statusArticle(mapToStatusArticle(articleRequest.getStatusArticle()))
                .createdAt(LocalDate.now())
                .approvedBy(List.of())
                .build();
        articleRepository.save(article);
    }

    @Override
    public void updateArticle(Long id, ArticleRequest article) {
        Article article1 = articleRepository.findById(id).orElseThrow();
        article1.setEditorsId(article.getEditorsId());
        article1.setTitle(article.getTitle());
        article1.setContent(article.getContent());
        articleRepository.save(article1);
    }

    @Override
    public void changeStatus(Long id, String status) {
        Article article = articleRepository.findById(id).orElseThrow();

        StatusArticle statusArticle = mapToStatusArticle(status);
        if (statusArticle.equals(StatusArticle.PUBLISHED) && article.getApprovedBy().isEmpty()) {
            throw new IllegalArgumentException("Article is not approved by any reviewer");
        }
        article.setStatusArticle(statusArticle);
        articleRepository.save(article);
    }

    private ArticleResponse mapToArticleResponse(Article article) {
        return ArticleResponse.builder()
                .editorsId(article.getEditorsId())
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }
    private StatusArticle mapToStatusArticle(String statusArticle) {
        return StatusArticle.valueOf(statusArticle);
    }
    @Override
    public List<ArticleResponse> getArticlesWithFilter(String content, Long editorsId, LocalDate date) {
        List<Article> articles = articleRepository.findByFilters(content, editorsId, date);
        return articles.stream().map(this::mapToArticleResponse).toList();
    }

    @Override
    public void checkIfRoleIsEditor(String role) {
        if (!role.equals("EDITOR")) {
            throw new IllegalArgumentException("Role is not editor");
        }
    }
}
