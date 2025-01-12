package be.pxl.services.controller;

import be.pxl.services.domain.DTO.ReviewRequest;
import be.pxl.services.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    private final IReviewService reviewService;

    @GetMapping
    public ResponseEntity<?> getReview() {
        log.info("Fetching all reviews.");
        var reviews = reviewService.getAllReview();
        log.debug("Retrieved {} reviews.", reviews.size());
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        log.info("Deleting review with ID: {}", id);
        reviewService.deleteReview(id);
        log.debug("Review with ID {} deleted successfully.", id);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getReviewOfPost(@PathVariable Long postId) {
        log.info("Fetching reviews for post ID: {}", postId);
        var reviews = reviewService.getReviewOfPost(postId);
        log.debug("Retrieved {} reviews for post ID {}.", reviews.size(), postId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addReview(@RequestBody ReviewRequest reviewRequest) {
        log.info("Adding a new review for post ID: {}", reviewRequest.getPostId());
        log.debug("Review details: {}", reviewRequest);
        reviewService.addReview(reviewRequest);
        log.debug("Review added successfully for post ID: {}", reviewRequest.getPostId());
    }
}
