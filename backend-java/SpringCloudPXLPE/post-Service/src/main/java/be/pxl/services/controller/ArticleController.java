package be.pxl.services.controller;

import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.domain.dto.ArticleResponse;
import be.pxl.services.services.IArticleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final IArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getArticle() {
        log.info("Fetching all articles.");
        List<ArticleResponse> articles = articleService.getAllArticles();
        log.debug("Retrieved {} articles.", articles.size());
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id) {
        log.info("Fetching article with ID: {}", id);
        ArticleResponse article = articleService.getArticleById(id);
        log.debug("Retrieved article: {}", article);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<ArticleResponse>> getArticleByStatus(@PathVariable String status) {
        log.info("Fetching articles with status: {}", status);
        List<ArticleResponse> articles = articleService.getAllArticlesByStatus(status);
        log.debug("Retrieved {} articles with status {}.", articles.size(), status);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/{status}/{editorsId}")
    public ResponseEntity<List<ArticleResponse>> getArticleByStatus(@PathVariable String status, @PathVariable String editorsId) {
        log.info("Fetching articles with status '{}' for editor ID '{}'.", status, editorsId);
        List<ArticleResponse> articles = articleService.getAllArticlesOfEditorByStatus(status, editorsId);
        log.debug("Retrieved {} articles with status '{}' for editor ID '{}'.", articles.size(), status, editorsId);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody ArticleRequest articleRequest, @RequestHeader String role) {
        log.info("Adding a new article. Role: {}", role);
        articleService.checkIfRoleIsEditor(role);
        ArticleResponse response = articleService.addArticle(articleRequest);
        log.debug("Article created with response: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateArticle(@PathVariable Long id, @RequestBody ArticleRequest articleRequest, @RequestHeader String role) {
        log.info("Updating article with ID: {}. Role: {}", id, role);
        articleService.checkIfRoleIsEditor(role);
        articleService.updateArticle(id, articleRequest);
        log.debug("Article with ID {} updated successfully.", id);
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<ArticleResponse> changeStatus(@PathVariable Long id, @PathVariable String status) {
        log.info("Changing status of article ID {} to '{}'.", id, status);
        ArticleResponse articleResponse = articleService.changeStatus(id, status);
        log.debug("Article ID {} status changed to '{}'. Response: {}", id, status, articleResponse);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(articleResponse);
    }

    @PutMapping("/Notif/{id}")
    public void resetNotification(@PathVariable Long id) {
        log.info("Resetting notifications for article ID {}.", id);
        articleService.resetNotification(id);
        log.debug("Notifications reset for article ID {}.", id);
    }
}
