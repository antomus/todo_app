package com.mongo.todo.repository;

import com.mongo.todo.model.CryptoCurrency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CryptoCurrencyRepository extends MongoRepository<CryptoCurrency, String> {
    Page<CryptoCurrency> findAllByCode(String code, Pageable paging);

    Optional<CryptoCurrency> findTopByCodeOrderByPriceDesc(String code);

    Optional<CryptoCurrency>  findTopByCodeOrderByPriceAsc(String code);
}
