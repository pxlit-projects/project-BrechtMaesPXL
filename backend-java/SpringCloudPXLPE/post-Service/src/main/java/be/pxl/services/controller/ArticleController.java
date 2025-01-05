package be.pxl.services.controller;

import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.domain.dto.ArticleResponse;
import be.pxl.services.services.IArticleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {


    private final IArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getArticle(){
        return new ResponseEntity<List<ArticleResponse>>(articleService.getAllArticles(), HttpStatus.OK);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addArticle(@RequestBody ArticleRequest articleRequest , @RequestHeader String role){
        articleService.addArticle(articleRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void updateArticle(@PathVariable Long id, @RequestBody ArticleRequest articleRequest , @RequestHeader String role){
        articleService.updateArticle(id, articleRequest);
    }
    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void changeStatus(@PathVariable Long id, @RequestBody String status){
        articleService.changeStatus(id, status);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<ArticleResponse>> getArticleWithFilter(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Long editorsId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ArticleResponse> filteredArticles = articleService.getArticlesWithFilter(content, editorsId, date);
        return new ResponseEntity<>(filteredArticles, HttpStatus.OK);
    }
}
