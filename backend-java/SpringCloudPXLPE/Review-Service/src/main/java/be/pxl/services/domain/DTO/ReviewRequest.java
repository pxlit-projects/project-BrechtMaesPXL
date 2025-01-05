package be.pxl.services.domain.DTO;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Long editorsId;
    private Long postId;
    private String title;
    private Long content;
}
