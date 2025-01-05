package be.pxl.services.controller;

import be.pxl.services.domain.dto.ArticleRequest;
import be.pxl.services.services.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {


    private final IArticleService articleService;

    @GetMapping
    public ResponseEntity getArticle(){
        return new ResponseEntity(articleService.getAllArticles(), HttpStatus.OK);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addArticle(@RequestBody ArticleRequest articleRequest){
        articleService.addArticle(articleRequest);
    }
}
