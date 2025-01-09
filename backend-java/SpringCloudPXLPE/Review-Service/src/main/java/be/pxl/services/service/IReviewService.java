package be.pxl.services.service;

import be.pxl.services.domain.DTO.ReviewRequest;
import be.pxl.services.domain.DTO.ReviewResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IReviewService {
    List<ReviewResponse> getAllReview();
    List<ReviewResponse> getReviewOfPost( Long postId);

    void deleteReview(Long id);

    void addReview(ReviewRequest reviewRequest);
}
