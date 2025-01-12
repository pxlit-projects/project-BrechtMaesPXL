package be.pxl.services;

import be.pxl.services.client.PostClientComment;
import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.repository.CommentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class CommentControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private PostClientComment postClient;
    @Autowired
    private CommentRepository commentRepository;

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("comment");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Test
    void testGetComments() throws Exception {
        // Arrange
        commentRepository.save(Comment.builder().content("First comment").postId(1L).editorsId("editor1").build());
        commentRepository.save(Comment.builder().content("Second comment").postId(2L).editorsId("editor2").build());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].content", is("First comment")))
                .andExpect(jsonPath("$[1].content", is("Second comment")));
    }

    @Test
    void testGetCommentsByPostId() throws Exception {
        // Arrange
        commentRepository.save(Comment.builder().content("Comment for Post 1").postId(1L).editorsId("editor1").build());
        commentRepository.save(Comment.builder().content("Another Comment for Post 1").postId(1L).editorsId("editor2").build());
        commentRepository.save(Comment.builder().content("Comment for Post 2").postId(2L).editorsId("editor3").build());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comment/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].content", is("Comment for Post 1")))
                .andExpect(jsonPath("$[1].content", is("Another Comment for Post 1")));
    }

    @Test
    void testDeleteComment() throws Exception {
        // Arrange
        Comment comment = commentRepository.save(Comment.builder().content("To be deleted").postId(3L).editorsId("editor3").build());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comment/" + comment.getId()))
                .andExpect(status().isOk());

        // Verify
        assertEquals(0, commentRepository.findAll().size());
    }

    @Test
    void testUpdateComment() throws Exception {
        // Arrange
        Comment comment = commentRepository.save(Comment.builder().content("Old content").postId(4L).editorsId("editor4").build());

        String updatedContent = objectMapper.writeValueAsString(
                Comment.builder().content("Updated content").postId(4L).editorsId("editor4").build());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/comment/" + comment.getId())
                        .contentType("application/json")
                        .content(updatedContent))
                .andExpect(status().isOk());

        // Verify
        Comment updatedComment = commentRepository.findById(comment.getId()).orElseThrow();
        assertEquals("Updated content", updatedComment.getContent());
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
    }

    @Test
    void testAddComment() throws Exception {
        // Arrange
        Long postId = 1L;
        CommentRequest commentRequest = CommentRequest.builder()
                .content("New comment")
                .editorsId("editor1")
                .postId(postId)
                .build();

        // Mock the PostClientComment to simulate that the post exists
        when(postClient.getArticleById(postId)).thenReturn(ResponseEntity.ok().build());

        String commentJson = objectMapper.writeValueAsString(commentRequest);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment")
                        .contentType("application/json")
                        .content(commentJson))
                .andExpect(status().isCreated());

        // Verify the comment was saved in the repository
        Comment savedComment = commentRepository.findAll().get(0);
        assertEquals("New comment", savedComment.getContent());
        assertEquals("editor1", savedComment.getEditorsId());
    }
}