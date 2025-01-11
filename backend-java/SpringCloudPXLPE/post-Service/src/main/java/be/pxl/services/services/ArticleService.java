package be.pxl.services.services;

import be.pxl.services.domain.Article;
import be.pxl.services.domain.StatusArticle;
import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.domain.dto.ArticleResponse;
import be.pxl.services.domain.dto.ReviewRMessage;
import be.pxl.services.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ArticleService implements IArticleService{

    private  final ArticleRepository articleRepository;



    @Override
    public List<ArticleResponse> getAllArticles() {
         List<Article> article = articleRepository.findAll();
        return article.stream().map(this::mapToArticleResponse).toList();
    }

    @Override
    public ArticleResponse getArticleById(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        return mapToArticleResponse(article);
    }

    @Override
    public List<ArticleResponse> getAllArticlesByStatus(String status) {
        List<Article> articles = articleRepository.findAllByStatusArticle(mapToStatusArticle(status));
        return articles.stream().map(this::mapToArticleResponse).toList();
    }

    @Override
    public List<ArticleResponse> getAllArticlesOfEditorByStatus(String status, String editorsId) {
        List<Article> articles = articleRepository.findAllOfEditorByStatusArticle(mapToStatusArticle(status), editorsId);
        return articles.stream().map(this::mapToArticleResponse).toList();
    }

    @Override
    public ArticleResponse addArticle(ArticleRequest articleRequest) {
        Article article = Article.builder()
                .editorsId(articleRequest.getEditorsId())
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .statusArticle(mapToStatusArticle(articleRequest.getStatus()))
                .createdAt(LocalDate.now())
                .approvedBy(List.of())
                .rejectedBy(List.of())
                .Notification(0)
                .build();
        articleRepository.save(article);
        return mapToArticleResponse(article);
    }

    @Override
    @RabbitListener(queues = "messaging-queue")
    @Transactional
    public void addApprovedBy(ReviewRMessage reviewMessage) {

        Article article = articleRepository.findById(reviewMessage.getPostId()).orElseThrow();
        List<String> approvedBy = article.getApprovedBy();
        approvedBy.add(reviewMessage.getEditorId());
        article.setApprovedBy(approvedBy);
        article.setNotification(article.getNotification() + 1);
        articleRepository.save(article);
    }

    @Override
    @RabbitListener(queues = "reject-queue")
    @Transactional
    public void addRejectedBy(ReviewRMessage reviewMessage) {
        Article article = articleRepository.findById(reviewMessage.getPostId()).orElseThrow();
        List<String> rejectedBy = article.getRejectedBy();
        rejectedBy.add(reviewMessage.getEditorId());
        article.setRejectedBy(rejectedBy);
        article.setNotification(article.getNotification() + 1);
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
    public ArticleResponse changeStatus(Long id, String status) {

//        ArticleResponse article = getArticleById(id);
        Article article = articleRepository.findById(id).orElseThrow();

        StatusArticle statusArticle = mapToStatusArticle(status);
        if (statusArticle.equals(StatusArticle.PUBLISHED) && article.getApprovedBy().isEmpty()) {
            throw new IllegalArgumentException("Article is not approved by any reviewer");
        }
        article.setStatusArticle(statusArticle);
        articleRepository.save(article);
        return mapToArticleResponse(article);
    }

    private ArticleResponse mapToArticleResponse(Article article) {
        return ArticleResponse.builder()
                .editorsId(article.getEditorsId())
                .title(article.getTitle())
                .content(article.getContent())
                .approvedBy(article.getApprovedBy())
                .createdAt(article.getCreatedAt())
                .id(article.getId().toString())
                .rejectedBy(article.getRejectedBy())
                .statusArticle(article.getStatusArticle() != null
                        ? mapStatusArticleToString(article.getStatusArticle())
                        : "UNKNOWN")
                .Notification(article.getNotification())
                .build();
    }

    private StatusArticle mapToStatusArticle(String statusArticle) {
        try {
            return StatusArticle.valueOf(statusArticle.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid status provided: " + statusArticle + ". Allowed values are: "
                            + Arrays.toString(StatusArticle.values())
            );
        }
    }

    private String mapStatusArticleToString(StatusArticle statusArticle) {
        return switch (statusArticle) {
            case PUBLISHED -> "PUBLISHED";
            case DELETED -> "REJECTED";
            case DRAFT -> "DRAFT";
            case REVIEW -> "REVIEW";
            default -> throw new IllegalArgumentException("Status not found");
        };
    }


    @Override
    public void checkIfRoleIsEditor(String role) {
        if (!role.equals("EDITOR")) {
            throw new IllegalArgumentException("Role is not editor");
        }
    }

    @Override
    public void resetNotification(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        article.setNotification(0);
        articleRepository.save(article);
    }


}
