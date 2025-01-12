package be.pxl.services.domain.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest( classes = ArticleResponse.class)

public class ArticleResponseTest {

    @Test
    public void testArticleResponseConstructorAndGetterSetter() {
        ArticleResponse articleResponse = new ArticleResponse("1", "editor123", LocalDate.now(), "Article Title", "Content", "PUBLISHED", List.of("editor456"), List.of("editor789"), 1);

        assertThat(articleResponse.getId()).isEqualTo("1");
        assertThat(articleResponse.getEditorsId()).isEqualTo("editor123");
        assertThat(articleResponse.getCreatedAt()).isNotNull();
        assertThat(articleResponse.getTitle()).isEqualTo("Article Title");
        assertThat(articleResponse.getContent()).isEqualTo("Content");
        assertThat(articleResponse.getStatusArticle()).isEqualTo("PUBLISHED");
        assertThat(articleResponse.getApprovedBy()).containsExactly("editor456");
        assertThat(articleResponse.getRejectedBy()).containsExactly("editor789");
        assertThat(articleResponse.getNotification()).isEqualTo(1);
    }

    @Test
    public void testArticleResponseBuilder() {
        ArticleResponse articleResponseWithBuilder = ArticleResponse.builder()
                .id("2")
                .editorsId("editor987")
                .createdAt(LocalDate.now())
                .title("New Title")
                .content("New Content")
                .statusArticle("DRAFT")
                .approvedBy(List.of("editor123"))
                .rejectedBy(List.of("editor456"))
                .Notification(2)
                .build();

        assertThat(articleResponseWithBuilder.getId()).isEqualTo("2");
        assertThat(articleResponseWithBuilder.getEditorsId()).isEqualTo("editor987");
        assertThat(articleResponseWithBuilder.getTitle()).isEqualTo("New Title");
        assertThat(articleResponseWithBuilder.getContent()).isEqualTo("New Content");
        assertThat(articleResponseWithBuilder.getStatusArticle()).isEqualTo("DRAFT");
        assertThat(articleResponseWithBuilder.getApprovedBy()).containsExactly("editor123");
        assertThat(articleResponseWithBuilder.getRejectedBy()).containsExactly("editor456");
        assertThat(articleResponseWithBuilder.getNotification()).isEqualTo(2);
    }
}
