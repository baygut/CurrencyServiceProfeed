package com.baygut.CurrencyService.repository;

import com.baygut.CurrencyService.model.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface CurrencyRepository extends MongoRepository<Currency, String> {
    List<Currency> findByTarget(String target);

    List<Currency> findBySource(String source);

    List<Currency> findByTime(Date time);
}
