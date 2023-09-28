package com.news.newspublisherapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Date;

@PrimaryKeyClass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyNewsPaperKey implements Serializable {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    Date date;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    String region;
}
