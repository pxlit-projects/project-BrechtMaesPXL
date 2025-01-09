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
    @GetMapping("/id/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id){
        return new ResponseEntity<ArticleResponse>(articleService.getArticleById(id), HttpStatus.OK);
    }
    @GetMapping("/{status}")
    public ResponseEntity<List<ArticleResponse>> getArticleByStatus(@PathVariable String status){
        return new ResponseEntity<List<ArticleResponse>>(articleService.getAllArticlesByStatus(status), HttpStatus.OK);
    }
    @GetMapping("/{status}/{editorsId}")
    public ResponseEntity<List<ArticleResponse>> getArticleByStatus(@PathVariable String status, @PathVariable String editorsId){
        return new ResponseEntity<List<ArticleResponse>>(articleService.getAllArticlesOfEditorByStatus(status, editorsId), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ArticleResponse>  addArticle(@RequestBody ArticleRequest articleRequest , @RequestHeader String role){

        articleService.checkIfRoleIsEditor(role);
        ArticleResponse response = articleService.addArticle(articleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateArticle(@PathVariable Long id, @RequestBody ArticleRequest articleRequest , @RequestHeader String role){
        articleService.checkIfRoleIsEditor(role);
        articleService.updateArticle(id, articleRequest);
    }
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<ArticleResponse>  changeStatus(@PathVariable Long id, @PathVariable String status){
        ArticleResponse articleResponse = articleService.changeStatus(id, status);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(articleResponse);
    }

}
