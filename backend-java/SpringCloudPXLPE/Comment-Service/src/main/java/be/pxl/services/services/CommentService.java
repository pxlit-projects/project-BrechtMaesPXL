package be.pxl.services.services;

import be.pxl.services.client.PostClientComment;
import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private  final CommentRepository commentRepository;
    private final PostClientComment postClient;

    @Override
    public List<CommentResponse> getAllComment() {
         List<Comment> Comments = commentRepository.findAll();
        return Comments.stream().map(this::mapToArticleResponse).toList();
    }

    @Override
    public List<CommentResponse> getCommentByPostId(Long id) {
        List<Comment> Comments = commentRepository.findAllCommentsByPostId(id);
        return Comments.stream().map(this::mapToArticleResponse).toList();
    }

    private CommentResponse mapToArticleResponse(Comment comment) {
        return CommentResponse.builder()
                .editorsId(comment.getEditorsId())
                .content(comment.getContent())
                .postId(comment.getPostId())
                .build();
    }
    @Override
    public void addComment(CommentRequest commentRequest) {
        if (!checkIfPostExist(commentRequest.getPostId())) {
            throw new IllegalArgumentException("Post does not exist");
        }
        Comment comment = Comment.builder()
                .editorsId(commentRequest.getEditorsId())
                .content(commentRequest.getContent())
                .postId(commentRequest.getPostId())
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void UpdateComment(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        comment.setEditorsId(commentRequest.getEditorsId());
        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
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

}
