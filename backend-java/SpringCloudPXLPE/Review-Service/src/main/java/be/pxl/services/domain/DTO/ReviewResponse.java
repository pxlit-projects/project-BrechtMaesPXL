package be.pxl.services.domain.DTO;

import be.pxl.services.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private Type type;
    private String editorsId;
    private String postId;
    private String title;
    private String content;
}
