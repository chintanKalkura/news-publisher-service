package com.news.newsvalidationapi.dto;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class ValidationReport {
    @Id
    String reportId;
    String articleId;
    String recommendation;
}
