package be.pxl.services.services;

import be.pxl.services.domain.Article;
import be.pxl.services.domain.StatusArticle;
import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.domain.dto.ArticleResponse;
import be.pxl.services.domain.dto.ReviewRMessage;
import be.pxl.services.repository.ArticleRepository;
import be.pxl.services.services.ArticleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    public ArticleServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllArticles() {
        // Arrange
        Article article1 = Article.builder().id(1L).title("Title 1").build();
        Article article2 = Article.builder().id(2L).title("Title 2").build();
        when(articleRepository.findAll()).thenReturn(List.of(article1, article2));

        // Act
        List<ArticleResponse> result = articleService.getAllArticles();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void testGetArticleById() {
        // Arrange
        Article article = Article.builder().id(1L).title("Test Article").build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // Act
        ArticleResponse response = articleService.getArticleById(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Test Article", response.getTitle());
        verify(articleRepository, times(1)).findById(1L);
    }



    @Test
    void testChangeStatusToPublishedWithoutApprovalThrowsException() {
        // Arrange
        Article article = Article.builder().id(1L).approvedBy(List.of()).build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            articleService.changeStatus(1L, "PUBLISHED");
        });
        assertEquals("Article is not approved by any reviewer", exception.getMessage());
        verify(articleRepository, times(1)).findById(1L);
    }

    @Test
    void testChangeStatus() {
        // Arrange
        Article article = Article.builder().id(1L).approvedBy(List.of("Editor1")).build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // Act
        ArticleResponse response = articleService.changeStatus(1L, "PUBLISHED");

        // Assert
        assertNotNull(response);
        assertEquals("PUBLISHED", response.getStatusArticle());
        verify(articleRepository, times(1)).save(article);
    }

//    @Test
//    void testAddApprovedBy() {
//        // Arrange
//        ReviewRMessage reviewMessage = new ReviewRMessage(1L, "Editor1");
//        Article article = Article.builder().id(1L).approvedBy(List.of()).Notification(0).build();
//        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
//
//        // Act
//        articleService.addApprovedBy(reviewMessage);
//
//        // Assert
//        assertEquals(1, article.getNotification());
//        assertEquals(1, article.getApprovedBy().size());
//        verify(articleRepository, times(1)).save(article);
//    }

//    @Test
//    void testAddRejectedBy() {
//        // Arrange
//        ReviewRMessage reviewMessage = new ReviewRMessage(1L, "1");
//        Article article = Article.builder()
//                .id(1L)
//                .rejectedBy(List.of())
//                .statusArticle(StatusArticle.DRAFT)
//                .editorsId("1")
//                .title("Article 1")
//                .content("Content of article 1")
//                .approvedBy(List.of())
//                .createdAt(LocalDate.now())
//                .Notification(0)
//                .build();
//        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
//
//        // Act
//        articleService.addRejectedBy(reviewMessage);
//
//        // Assert
//        assertEquals(1, article.getNotification());
//        assertEquals(1, article.getRejectedBy().size());
//        verify(articleRepository, times(1)).save(article);
//    }

    @Test
    void testResetNotification() {
        // Arrange
        Article article = Article.builder().id(1L).Notification(5).build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // Act
        articleService.resetNotification(1L);

        // Assert
        assertEquals(0, article.getNotification());
        verify(articleRepository, times(1)).save(article);
    }

    @Test
    void testCheckIfRoleIsEditorThrowsExceptionForNonEditor() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            articleService.checkIfRoleIsEditor("ADMIN");
        });
        assertEquals("Role is not editor", exception.getMessage());
    }

    @Test
    void testCheckIfRoleIsEditorPassesForEditor() {
        // Act & Assert
        assertDoesNotThrow(() -> articleService.checkIfRoleIsEditor("EDITOR"));
    }
}
