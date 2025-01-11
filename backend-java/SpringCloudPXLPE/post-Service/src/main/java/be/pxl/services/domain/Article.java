package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "article")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String editorsId;

    private String title;
    private String content;
    private LocalDate createdAt;

    private StatusArticle statusArticle;

    @ElementCollection
    private List<String> approvedBy;

    @ElementCollection
    private List<String> rejectedBy;

    private int Notification;
}
