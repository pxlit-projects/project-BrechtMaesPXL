package be.pxl.services.services;

import be.pxl.services.client.PostClient;
import be.pxl.services.domain.DTO.ReviewMessage;
import be.pxl.services.domain.DTO.ReviewRequest;
import be.pxl.services.domain.DTO.ReviewResponse;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.Type;
import be.pxl.services.repository.ReviewRepository;
import be.pxl.services.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PostClient postClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReview() {
        // Arrange
        Review review1 = Review.builder()
                .id(1L)
                .editorsId("Editor1")
                .postId(1L)
                .title("Title 1")
                .content("Content 1")
                .type(Type.ACCEPTED)
                .build();
        Review review2 = Review.builder()
                .id(2L)
                .editorsId("Editor2")
                .postId(2L)
                .title("Title 2")
                .content("Content 2")
                .type(Type.REJECTED)
                .build();
        when(reviewRepository.findAll()).thenReturn(List.of(review1, review2));

        // Act
        List<ReviewResponse> result = reviewService.getAllReview();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Editor1", result.get(0).getEditorsId());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testGetReviewOfPost() {
        // Arrange
        Long postId = 1L;
        Review review1 = Review.builder().id(1L).postId(postId).build();
        Review review2 = Review.builder().id(2L).postId(postId).build();
        when(reviewRepository.findAllReviewsByPostId(postId)).thenReturn(List.of(review1, review2));

        // Act
        List<ReviewResponse> result = reviewService.getReviewOfPost(postId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(postId.toString(), result.get(0).getPostId());
        verify(reviewRepository, times(1)).findAllReviewsByPostId(postId);
    }

    @Test
    void testDeleteReview() {
        // Arrange
        Long reviewId = 1L;

        // Act
        reviewService.deleteReview(reviewId);

        // Assert
        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void testAddReviewAccepted() {
        // Arrange
        ReviewRequest request = new ReviewRequest("Editor1", 1L, "ACCEPTED", "ACCEPTED", "1L");
        when(postClient.getArticleById(1L)).thenReturn(ResponseEntity.ok().build());
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        reviewService.addReview(request);

        // Assert
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(rabbitTemplate, times(1)).convertAndSend(eq("messaging-queue"), any(ReviewMessage.class));
        verify(rabbitTemplate, times(0)).convertAndSend(eq("reject-queue"), any(ReviewMessage.class));
    }

    @Test
    void testAddReviewRejected() {
        // Arrange
        ReviewRequest request = new ReviewRequest("Editor2", 1L, "REJECTED", "REJECTED", "1L");
        when(postClient.getArticleById(1L)).thenReturn(ResponseEntity.ok().build());
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        reviewService.addReview(request);

        // Assert
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(rabbitTemplate, times(0)).convertAndSend(eq("messaging-queue"), any(ReviewMessage.class));
        verify(rabbitTemplate, times(1)).convertAndSend(eq("reject-queue"), any(ReviewMessage.class));
    }

    @Test
    void testAddReviewPostDoesNotExistThrowsException() {
        // Arrange
        ReviewRequest request = new ReviewRequest("Editor3", 2L, "Content", "ACCEPTED", "2L");
        when(postClient.getArticleById(2L)).thenThrow(new RuntimeException("Post not found"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reviewService.addReview(request));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testCheckIfPostExist() {
        // Arrange
        Long postId = 1L;
        when(postClient.getArticleById(postId)).thenReturn(ResponseEntity.ok().build());

        // Act
        Boolean result = reviewService.checkIfPostExist(postId);

        // Assert
        assertTrue(result);
        verify(postClient, times(1)).getArticleById(postId);
    }

    @Test
    void testCheckIfPostDoesNotExist() {
        // Arrange
        Long postId = 1L;
        when(postClient.getArticleById(postId)).thenThrow(new RuntimeException("Post not found"));

        // Act
        Boolean result = reviewService.checkIfPostExist(postId);

        // Assert
        assertFalse(result);
        verify(postClient, times(1)).getArticleById(postId);
    }
}
