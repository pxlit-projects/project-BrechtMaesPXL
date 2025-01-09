package be.pxl.services.domain.DTO;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private String editorsId;
    private long postId;
    private String type;
    private String title;
    private String content;
}
