package be.pxl.services.controller;

import be.pxl.services.domain.DTO.ReviewRequest;
import be.pxl.services.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin()
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;

    @GetMapping
    public ResponseEntity getReview(){
        return new ResponseEntity(reviewService.getAllReview(), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity getReviewOfPost(@PathVariable Long postId){
        return new ResponseEntity(reviewService.getReviewOfPost(postId), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addReview(@RequestBody ReviewRequest reviewRequest){
        reviewService.addReview(reviewRequest);
    }

}
