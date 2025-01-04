package be.pxl.services.services;

import be.pxl.services.domain.dto.CommentRequest;
import be.pxl.services.domain.dto.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICommentService {

    List<CommentResponse> getAllComment();

    void addComment(CommentRequest commentRequest);
}
