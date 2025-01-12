package be.pxl.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.repository.CommentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class CommentTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Container
    private  static MySQLContainer mySQLContainer =
            new MySQLContainer("mysql:8.0.26")
            .withDatabaseName("comment");

    @Autowired
    private CommentRepository commentRepository;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Test
    public void testCreateComment() throws Exception {
        Comment comment = Comment.builder()
                .id(1L)
                .content("This is a comment")
                .postId(1L)
                .editorsId("1")
                .build();

        String commentString = objectMapper.writeValueAsString(comment);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment")
                .contentType("application/json")
                .content(commentString))
                .andExpect(status().isCreated());
        assertEquals(1, commentRepository.findAll().size());
    }
}
