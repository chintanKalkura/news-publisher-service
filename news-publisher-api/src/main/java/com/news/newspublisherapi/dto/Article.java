package com.news.newspublisherapi.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Article {
    @Pattern(regexp = CONSTANTS.UUID_REG_EXP)
    @Id
    String id;
    @NotNull
    String name;
    @NotNull
    String text;
    @NotNull
    ArticleStatus articleStatus;
    String recommendation;

}
