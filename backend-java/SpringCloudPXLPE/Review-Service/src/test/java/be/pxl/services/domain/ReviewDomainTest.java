package be.pxl.services.domain;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ReviewDomainTest {
    @Test
    void testReviewBuilderAndGetters() {
        // Arrange
        Review review = Review.builder()
                .id(1L)
                .type(Type.ACCEPTED)
                .editorsId("123")
                .postId(456L)
                .title("Great Review")
                .content("This is the content of the review.")
                .build();

        // Assert
        assertEquals(Optional.of(1L), Optional.of(review.getId()));
        assertEquals(Type.ACCEPTED, review.getType());
        assertEquals("123", review.getEditorsId());
        assertEquals(Optional.of(456L), Optional.of(review.getPostId()));
        assertEquals("Great Review", review.getTitle());
        assertEquals("This is the content of the review.", review.getContent());
    }

    @Test
    void testReviewSetters() {
        // Arrange
        Review review = new Review();

        // Act
        review.setId(1L);
        review.setType(Type.REJECTED);
        review.setEditorsId("456");
        review.setPostId(789L);
        review.setTitle("Another Review");
        review.setContent("Updated content.");

        // Assert
        assertEquals(Optional.of(1L), Optional.of(review.getId()));
        assertEquals(Type.REJECTED, review.getType());
        assertEquals("456", review.getEditorsId());
        assertEquals(Optional.of(789L), Optional.of(review.getPostId()));
        assertEquals("Another Review", review.getTitle());
        assertEquals("Updated content.", review.getContent());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Review review1 = Review.builder()
                .id(1L)
                .type(Type.ACCEPTED)
                .editorsId("123")
                .postId(456L)
                .title("Great Review")
                .content("This is the content of the review.")
                .build();

        Review review2 = Review.builder()
                .id(1L)
                .type(Type.ACCEPTED)
                .editorsId("123")
                .postId(456L)
                .title("Great Review")
                .content("This is the content of the review.")
                .build();

        // Assert
        assertEquals(review1, review2);
        assertEquals(review1.hashCode(), review2.hashCode());
    }

    @Test
    void testNotEquals() {
        // Arrange
        Review review1 = Review.builder()
                .id(1L)
                .type(Type.ACCEPTED)
                .editorsId("123")
                .postId(456L)
                .title("Great Review")
                .content("This is the content of the review.")
                .build();

        Review review2 = Review.builder()
                .id(2L)
                .type(Type.REJECTED)
                .editorsId("456")
                .postId(789L)
                .title("Different Review")
                .content("Completely different content.")
                .build();

        // Assert
        assertNotEquals(review1, review2);
    }
}