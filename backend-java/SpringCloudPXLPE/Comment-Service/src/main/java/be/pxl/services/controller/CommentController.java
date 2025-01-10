package be.pxl.services.controller;

import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin()
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {


    private final ICommentService commentService;

    @GetMapping
    public ResponseEntity getArticle(){
        return new ResponseEntity(commentService.getAllComment(), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addArticle(@RequestBody CommentRequest commentRequest){

        commentService.addComment(commentRequest);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity getArticleByPostId(@PathVariable Long id){
        return new ResponseEntity(commentService.getCommentByPostId(id), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id){
        commentService.deleteComment(id);
    }
    @PutMapping("/{id}")
    public void updateArticle(@PathVariable Long id, @RequestBody CommentRequest commentRequest){
        commentService.UpdateComment(id, commentRequest);
    }

}
