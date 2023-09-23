package com.news.newspublisherapi;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class Article {
    @Pattern(regexp = CONSTANTS.UUID_REG_EXP)
    String id;
    @NotNull
    String name;
    @NotNull
    String text;

}
