package com.news.newspublisherapi.service;

import com.news.newspublisherapi.dto.DailyNewsPaper;
import com.news.newspublisherapi.dto.DailyNewsPaperKey;
import org.springframework.data.repository.CrudRepository;

public interface DailyNewsPaperRepository extends CrudRepository<DailyNewsPaper, DailyNewsPaperKey> {
}
