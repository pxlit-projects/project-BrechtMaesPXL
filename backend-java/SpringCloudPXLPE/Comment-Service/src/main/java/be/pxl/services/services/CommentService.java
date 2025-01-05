package be.pxl.services.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import be.pxl.services.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private  final CommentRepository commentRepository;
    @Override
    public List<CommentResponse> getAllComment() {
         List<Comment> Comments = commentRepository.findAll();
        return Comments.stream().map(this::mapToArticleResponse).toList();
    }

    private CommentResponse mapToArticleResponse(Comment comment) {
        return CommentResponse.builder()
                .editorsId(comment.getEditorsId())
                .title(comment.getTitle())
                .content(comment.getContent())
                .build();
    }
    @Override
    public void addComment(CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .editorsId(commentRequest.getEditorsId())
                .title(commentRequest.getTitle())
                .content(commentRequest.getContent())
                .build();
        commentRepository.save(comment);
    }

    @Override
    public void UpdateComment(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        comment.setEditorsId(commentRequest.getEditorsId());
        comment.setTitle(commentRequest.getTitle());
        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);
    }
}
