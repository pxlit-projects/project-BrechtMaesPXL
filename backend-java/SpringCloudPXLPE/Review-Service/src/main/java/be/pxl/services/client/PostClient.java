package be.pxl.services.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-service")
public interface PostClient {

    @GetMapping("/api/article/id/{id}")
    ResponseEntity getArticleById(@PathVariable Long id);

}

