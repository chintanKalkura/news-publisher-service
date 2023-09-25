package com.news.newspublisherapi.dto;

import java.util.List;

public class CONSTANTS {

    public static final String UUID_REG_EXP = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final List<ArticleStatus> READER_AUTHORISED_FOR_ARTICLE_STATUS = List.of(ArticleStatus.PUBLISHED);
    public static final List<ArticleStatus> AUTHOR_AUTHORISED_FOR_ARTICLE_STATUS = List.of(ArticleStatus.REVIEWED_EDITOR, ArticleStatus.PUBLISHED);
    public static final List<ArticleStatus> EDITOR_AUTHORISED_FOR_ARTICLE_STATUS = List.of(ArticleStatus.IN_REVIEW_EDITOR,
            ArticleStatus.REVIEWED_LEGAL, ArticleStatus.IN_RE_REVIEW, ArticleStatus.REVIEWED_EDITOR, ArticleStatus.PUBLISHED);
}
