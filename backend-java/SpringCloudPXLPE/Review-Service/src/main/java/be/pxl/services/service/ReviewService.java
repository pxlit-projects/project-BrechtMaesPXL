package be.pxl.services.service;

import be.pxl.services.domain.DTO.ReviewRequest;
import be.pxl.services.domain.DTO.ReviewResponse;
import be.pxl.services.domain.Review;
import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService{

        private final ReviewRepository reviewRepository;

        @Override
        public List<ReviewResponse> getAllReview() {
            List<Review> reviews = reviewRepository.findAll();
            return reviews.stream().map(this::mapToReviewResponse).toList();
        }

    private ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .editorsId(review.getEditorsId())
                .title(review.getTitle())
                .content(review.getContent())
                .build();
    }

    @Override
    public void addReview(ReviewRequest reviewRequest) {
            Review review = Review.builder()
                    .editorsId(reviewRequest.getEditorsId())
                    .title(reviewRequest.getTitle())
                    .content(reviewRequest.getContent())
                    .build();
        reviewRepository.save(review);
    }
}
