package be.pxl.services.services;

import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.domain.dto.ArticleResponse;
import be.pxl.services.domain.dto.ReviewRMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IArticleService {

    List<ArticleResponse> getAllArticles();
    ArticleResponse getArticleById(Long id);
    List<ArticleResponse> getAllArticlesByStatus(String status);
    List<ArticleResponse> getAllArticlesOfEditorByStatus(String status, String editorsId);

    ArticleResponse addArticle(ArticleRequest article);
    void addApprovedBy(ReviewRMessage reviewMessage);
    void addRejectedBy(ReviewRMessage reviewMessage);


    void updateArticle(Long id, ArticleRequest article);
    ArticleResponse changeStatus(Long id, String status);

    void checkIfRoleIsEditor(String role);
}
