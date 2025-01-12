package be.pxl.services.domain;

import be.pxl.services.ReviewServiceApplication;
import be.pxl.services.domain.DTO.ReviewRequest;
import be.pxl.services.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ReviewServiceApplication.class)
@Testcontainers
@AutoConfigureMockMvc
public class ReviewTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("review");

    @Autowired
    private ReviewRepository reviewRepository;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    public void setUpDb (){

        Review review1 = Review.builder()
                .id(1L)
                .content("Content of review 1")
                .title("Review 1")
                .type(Type.ACCEPTED)
                .postId(120L)
                .editorsId("123")
                .build();
        Review review2 = Review.builder()
                .id(1L)
                .content("Content of review 1")
                .title("Review 1")
                .type(Type.ACCEPTED)
                .postId(123L)
                .editorsId("123")
                .build();
        reviewRepository.saveAll(List.of(review1, review2));
    }
    @Test
    public void testGetAllReviews() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/review"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Content of review 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Review 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("ACCEPTED"));
    }
    @Test
    public void testGetReviewsOfPost() throws Exception {
        // Assuming postId 123 exists and is linked with reviews
        mockMvc.perform(MockMvcRequestBuilders.get("/api/review/post/123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Content of review 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Review 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("ACCEPTED"));
    }



    @Test
    public void testDeleteReview() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/review/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }
    @Test
    public void testGetAllReviews_NoReviews() throws Exception {
        tearDown();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/review"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));     }

    @AfterEach
    public void tearDown() {
        reviewRepository.deleteAll();
    }






}
