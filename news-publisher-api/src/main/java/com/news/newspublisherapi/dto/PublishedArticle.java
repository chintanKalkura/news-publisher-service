package com.news.newspublisherapi.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

/*
 *TODO: this dto will be used after Article moves to published status. These will be stored in cassandra.
 * The articles will be published on website and on daily paper.
 * These will have comments thread.
 * */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@UserDefinedType("article")
public class PublishedArticle {
    @Pattern(regexp = CONSTANTS.UUID_REG_EXP)
    @Id
    String id;
    @NotNull
    String name;
    @NotNull
    String text;
}
