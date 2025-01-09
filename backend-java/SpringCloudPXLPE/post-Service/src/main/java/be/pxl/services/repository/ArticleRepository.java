package be.pxl.services.repository;

import be.pxl.services.domain.Article;
import be.pxl.services.domain.StatusArticle;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {


    List<Article> findAllByStatusArticle(StatusArticle statusArticle);

    @Query("SELECT a FROM Article a WHERE a.statusArticle = :statusArticle AND a.editorsId = :editorsId")
    List<Article> findAllOfEditorByStatusArticle(@Param("statusArticle") StatusArticle statusArticle,
                                                 @Param("editorsId") String editorsId);

}
