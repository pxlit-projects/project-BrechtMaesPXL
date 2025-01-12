package be.pxl.services.controller;

import be.pxl.services.domain.DTO.ReviewRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(    classes = ReviewRequest.class)
public class ReviewRequestTest {

    @Test
    public void testReviewRequestConstructorAndGetterSetter() {
        ReviewRequest reviewRequest = new ReviewRequest("123", 456L,"ACCEPTED", "Review Title", "789L");

        assertThat(reviewRequest.getEditorsId()).isEqualTo("123");
        assertThat(reviewRequest.getPostId()).isEqualTo(456L);
        assertThat(reviewRequest.getTitle()).isEqualTo("Review Title");
        assertThat(reviewRequest.getType()).isEqualTo("ACCEPTED");
        assertThat(reviewRequest.getContent()).isEqualTo("789L");

        ReviewRequest reviewRequestWithBuilder = ReviewRequest.builder()
                .editorsId("111")
                .postId(222L)
                .title("Another Review Title")
                .type("ACCEPTED")
                .content("333L")
                .build();

        assertThat(reviewRequestWithBuilder.getEditorsId()).isEqualTo("111");
        assertThat(reviewRequestWithBuilder.getPostId()).isEqualTo(222L);
        assertThat(reviewRequestWithBuilder.getTitle()).isEqualTo("Another Review Title");
        assertThat(reviewRequestWithBuilder.getType()).isEqualTo("ACCEPTED");
        assertThat(reviewRequestWithBuilder.getContent()).isEqualTo("333L");
    }
}
