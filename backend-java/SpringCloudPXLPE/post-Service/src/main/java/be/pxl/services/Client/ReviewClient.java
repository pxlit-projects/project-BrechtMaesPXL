package be.pxl.services.Client;
import be.pxl.services.domain.dto.ReviewRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "review-service")
public interface ReviewClient {

    @PostMapping("/review/")
    public void addReview(@RequestBody ReviewRequest reviewRequest);
}
