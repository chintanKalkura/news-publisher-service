package com.news.newspublisherapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;

import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.SET;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.UDT;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyNewsPaper {
    @PrimaryKey
    DailyNewsPaperKey dailyNewsPaperKey;
    @CassandraType(type=SET, typeArguments={UDT}, userTypeName = "article")
    @Column("entertainment_articles")
    Set<PublishedArticle> entertainmentArticles;
    @CassandraType(type=SET, typeArguments={UDT}, userTypeName = "article")
    @Column("sports_articles")
    Set<PublishedArticle> sportsArticles;
    @CassandraType(type=SET, typeArguments={UDT}, userTypeName = "article")
    @Column("politics_articles")
    Set<PublishedArticle> politicsArticles;
    @CassandraType(type=SET, typeArguments={UDT}, userTypeName = "article")
    @Column("regional_articles")
    Set<PublishedArticle> regionalArticles;
    @CassandraType(type=SET, typeArguments={UDT}, userTypeName = "article")
    Set<PublishedArticle> puzzles;
}
