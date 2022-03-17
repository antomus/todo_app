package com.mongo.todo.service;

import com.mongo.todo.model.CryptoCurrency;
import com.mongo.todo.repository.CryptoCurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class CryptoCurrencyService {
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    public CryptoCurrencyService(CryptoCurrencyRepository cryptoCurrencyRepository) {
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
    }

    public Page<CryptoCurrency> getAllRecordsByCode(String currencyCode, Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("price").ascending());
        return cryptoCurrencyRepository.findAllByCode(currencyCode, paging);
    }

    public Optional<CryptoCurrency> getMaxPriceRecord(String currencyCode) {
        return cryptoCurrencyRepository.findTopByCodeOrderByPriceDesc(currencyCode);
    }

    public Optional<CryptoCurrency> getMinPriceRecord(String currencyCode) {
        return cryptoCurrencyRepository.findTopByCodeOrderByPriceAsc(currencyCode);
    }
}
