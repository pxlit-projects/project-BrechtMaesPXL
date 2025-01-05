package be.pxl.services.controller;

import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void updateArticle(@PathVariable Long id, @RequestBody CommentRequest commentRequest){
        commentService.updateComment(id, commentRequest);
    }

}
