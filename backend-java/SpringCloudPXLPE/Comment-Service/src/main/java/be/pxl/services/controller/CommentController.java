package be.pxl.services.controller;

import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final ICommentService commentService;

    @GetMapping
    public ResponseEntity<?> getComment() {
        log.info("Fetching all comments.");
        var comments = commentService.getAllComment();
        log.debug("Retrieved {} comments.", comments.size());
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addComment(@RequestBody CommentRequest commentRequest) {
        log.info("Adding a new comment with content: '{}'", commentRequest.getContent());
        commentService.addComment(commentRequest);
        log.debug("Comment added successfully.");
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> getCommentByPostId(@PathVariable Long id) {
        log.info("Fetching comments for post ID: {}", id);
        var comments = commentService.getCommentByPostId(id);
        log.debug("Retrieved {} comments for post ID {}.", comments.size(), id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        log.info("Deleting comment with ID: {}", id);
        commentService.deleteComment(id);
        log.debug("Comment with ID {} deleted successfully.", id);
    }

    @PutMapping("/{id}")
    public void updateComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        log.info("Updating comment with ID: {}", id);
        log.debug("New comment content: '{}'", commentRequest.getContent());
        commentService.UpdateComment(id, commentRequest);
        log.debug("Comment with ID {} updated successfully.", id);
    }
}
