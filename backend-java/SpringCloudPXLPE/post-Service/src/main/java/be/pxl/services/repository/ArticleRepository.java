package be.pxl.services.repository;

import be.pxl.services.domain.Article;
import be.pxl.services.domain.StatusArticle;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE " +
            "(:content IS NULL OR a.content LIKE %:content%) AND " +
            "(:editorsId IS NULL OR a.editorsId = :editorsId) AND " +
            "(:date IS NULL OR a.createdAt = :date)")
    List<Article> findByFilters(@Param("content") String content,
                                @Param("editorsId") Long editorsId,
                                @Param("date") LocalDate date);
    List<Article> findAllByStatusArticle(StatusArticle statusArticle);

}
