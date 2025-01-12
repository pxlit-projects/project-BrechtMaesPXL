package be.pxl.services.domain.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest( classes = ArticleRequest.class)

public class ArticleRequestTest {

    @Test
    public void testArticleRequestConstructorAndGetterSetter() {
        ArticleRequest articleRequest = new ArticleRequest("editor123", "Article Title", "Content of the article", "DRAFT");

        assertThat(articleRequest.getEditorsId()).isEqualTo("editor123");
        assertThat(articleRequest.getTitle()).isEqualTo("Article Title");
        assertThat(articleRequest.getContent()).isEqualTo("Content of the article");
        assertThat(articleRequest.getStatus()).isEqualTo("DRAFT");

        ArticleRequest articleRequestWithBuilder = ArticleRequest.builder()
                .editorsId("editor456")
                .title("Another Title")
                .content("Different Content")
                .status("PUBLISHED")
                .build();

        assertThat(articleRequestWithBuilder.getEditorsId()).isEqualTo("editor456");
        assertThat(articleRequestWithBuilder.getTitle()).isEqualTo("Another Title");
        assertThat(articleRequestWithBuilder.getContent()).isEqualTo("Different Content");
        assertThat(articleRequestWithBuilder.getStatus()).isEqualTo("PUBLISHED");
    }

    @Test
    public void testArticleRequestNoArgsConstructor() {
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setEditorsId("editor789");
        articleRequest.setTitle("New Article");
        articleRequest.setContent("New content");
        articleRequest.setStatus("PENDING");

        assertThat(articleRequest.getEditorsId()).isEqualTo("editor789");
        assertThat(articleRequest.getTitle()).isEqualTo("New Article");
        assertThat(articleRequest.getContent()).isEqualTo("New content");
        assertThat(articleRequest.getStatus()).isEqualTo("PENDING");
    }
}
