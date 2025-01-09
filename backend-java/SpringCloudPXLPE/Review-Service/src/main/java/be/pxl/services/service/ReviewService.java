package be.pxl.services.service;

//import be.pxl.services.client.PostClient;
import be.pxl.services.client.PostClient;
import be.pxl.services.domain.DTO.ReviewMessage;
import be.pxl.services.domain.DTO.ReviewRequest;
import be.pxl.services.domain.DTO.ReviewResponse;
import be.pxl.services.domain.Review;
import be.pxl.services.domain.Type;
import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{

        private final ReviewRepository reviewRepository;
        private final PostClient postClient;
        private  final RabbitTemplate rabbitTemplate; // added


        @Override
        public List<ReviewResponse> getAllReview() {
            List<Review> reviews = reviewRepository.findAll();
            return reviews.stream().map(this::mapToReviewResponse).toList();
        }

    @Override
    public List<ReviewResponse> getReviewOfPost(Long postId) {
        List<Review> reviews = reviewRepository.findAllReviewsByPostId(postId);
        return reviews.stream().map(this::mapToReviewResponse).toList();
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId().toString())
                .editorsId(review.getEditorsId())
                .postId(review.getPostId().toString())
                .title(review.getTitle())
                .content(review.getContent())
                .type(review.getType())
                .build();
    }

    @Override
    public void addReview(ReviewRequest reviewRequest) {
        if (!checkIfPostExist(reviewRequest.getPostId())) {
            throw new IllegalArgumentException("Post does not exist");
        }
            Review review = Review.builder()
                    .editorsId(reviewRequest.getEditorsId())
                    .title(reviewRequest.getTitle())
                    .content(reviewRequest.getContent())
                    .type(mapToType(reviewRequest.getType().toUpperCase()))
                    .postId(reviewRequest.getPostId())
                    .build();
        reviewRepository.save(review);
        var message = new ReviewMessage(review.getPostId(), review.getEditorsId() );

        if (review.getType() == Type.ACCEPTED) {

            rabbitTemplate.convertAndSend("messaging-queue", message);
        }else {
            rabbitTemplate.convertAndSend("reject-queue", message);
        }
    }
    private Boolean checkIfPostExist(Long postId) {
        try {
            ResponseEntity<?> response = postClient.getArticleById(postId);
            if (response == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Type mapToType(String type) {
        return Type.valueOf(type);
    }

}
