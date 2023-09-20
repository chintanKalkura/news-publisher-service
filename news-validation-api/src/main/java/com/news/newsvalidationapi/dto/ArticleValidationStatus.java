package com.news.newsvalidationapi.dto;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
public class ArticleValidationStatus {
    @Id
    String articleId;
    @Enumerated
    ValidationStatus validationStatus;
}
