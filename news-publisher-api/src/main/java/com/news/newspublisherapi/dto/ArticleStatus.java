package com.news.newspublisherapi.dto;

public enum ArticleStatus {
    NEW,
    OPEN_FOR_REVIEW,
    IN_REVIEW_LEGAL,
    IN_REVIEW_EDITOR,
    REVIEWED_LEGAL,
    REVIEWED_EDITOR,
    RE_REVIEW,
    IN_RE_REVIEW,
    APPROVED,
    PUBLISHED
}
