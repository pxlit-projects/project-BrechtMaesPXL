package be.pxl.services.controller;

import be.pxl.services.PostServiceApplication;
import be.pxl.services.domain.Article;
import be.pxl.services.domain.Comment;
import be.pxl.services.domain.StatusArticle;
import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.repository.ArticleRepository;
import be.pxl.services.repository.CommentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest( classes = PostServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class PostTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("post");

    @Autowired
    private ArticleRepository articleRepository;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    public void setupDatabase() {

        Article article1 = Article.builder()
                .id(1L)
                .content("Content of article 1")
                .title("Article 1")
                .statusArticle(StatusArticle.DRAFT)
                .editorsId("1")
                .Notification(0)
                .approvedBy(List.of())
                .rejectedBy(List.of())
                .createdAt(java.time.LocalDate.now())

                .build();

        Article article2 = Article.builder()
                .id(2L)
                .content("Content of article 2")
                .title("Article 2")
                .statusArticle(StatusArticle.REVIEW)
                .editorsId("2")
                .Notification(0)
                .approvedBy(List.of())
                .rejectedBy(List.of())
                .createdAt(java.time.LocalDate.now())
                .build();

        articleRepository.saveAll(List.of(article1, article2));
    }

    @Test
    public void testGetAllArticles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/article"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Article 1"))
                .andExpect(jsonPath("$[1].title").value("Article 2"));
    }

    @Test
    public void testGetArticleById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/article/id/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Article 1"));
    }

    @Test
    public void testGetArticleByStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/article/DRAFT"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Article 1"));
    }

    @Test
    public void testGetArticleByStatusAndEditorId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/article/DRAFT/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Article 1"));
    }

    @Test
    public void testCreatePost() throws Exception {
        ArticleRequest post = ArticleRequest.builder()
                .content("New article content")
                .title("New Article")
                .status("DRAFT")
                .editorsId("3")
                .build();

        String postString = objectMapper.writeValueAsString(post);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/article")
                        .contentType("application/json")
                        .header("role", "EDITOR")
                        .content(postString))
                .andExpect(status().isCreated());

        assertEquals(3, articleRepository.findAll().size());
    }
    @Test
    public void testUpdateArticle() throws Exception {
        ArticleRequest post = ArticleRequest.builder()
                .content("Updated content")
                .title("Updated Article")
                .status("PUBLISHED")
                .editorsId("1")
                .build();

        String postString = objectMapper.writeValueAsString(post);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/article/update/1")
                        .contentType("application/json")
                        .header("role", "EDITOR")
                        .content(postString))
                .andExpect(status().isAccepted());

        Article updatedArticle = articleRepository.findById(1L).orElseThrow();
        assertEquals("Updated Article", updatedArticle.getTitle());
    }

    @Test
    public void testChangeArticleStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/article/1/status/REVIEW"))
                .andExpect(status().isAccepted());

        Article updatedArticle = articleRepository.findById(1L).orElseThrow();
        assertEquals(StatusArticle.REVIEW, updatedArticle.getStatusArticle());
    }

    @Test
    public void testResetNotification() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/article/Notif/2"))
                .andExpect(status().isOk());
    }

    @AfterEach
    public void cleanupDatabase() {
        articleRepository.deleteAll();
    }
}
