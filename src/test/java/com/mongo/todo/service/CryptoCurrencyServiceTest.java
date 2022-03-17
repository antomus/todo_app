package com.mongo.todo.service;

import com.mongo.todo.AllowedCurrency;
import com.mongo.todo.model.CryptoCurrency;
import com.mongo.todo.repository.CryptoCurrencyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@DataMongoTest
@ActiveProfiles({ "test" })
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CryptoCurrencyServiceTest {
    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    @DisplayName("Should return all records related to currency code")
    public void shouldReturnAllRecordsByCurrencyCode() {
        var currency = new CryptoCurrency("1",AllowedCurrency.BTC.toString(), 40000.0, LocalDateTime.now());
        cryptoCurrencyRepository.save(currency);

        CryptoCurrencyService cryptoCurrencyService = new CryptoCurrencyService(cryptoCurrencyRepository);
        var result  = cryptoCurrencyService.getAllRecordsByCode(AllowedCurrency.BTC.toString(), 0, 2);
        assertFalse(result == null);
        assertTrue(currency.getId().equals(result.getContent().get(0).getId()));
    }

    @Test
    void getMaxPriceRecord() {
        var record1 = new CryptoCurrency("11",AllowedCurrency.BTC.toString(), 40000.0, LocalDateTime.now());
        var record2 = new CryptoCurrency("12",AllowedCurrency.BTC.toString(), 39000.0, LocalDateTime.now());
        var record3 = new CryptoCurrency("13",AllowedCurrency.BTC.toString(), 38000.0, LocalDateTime.now());
        cryptoCurrencyRepository.save(record1);
        cryptoCurrencyRepository.save(record2);
        cryptoCurrencyRepository.save(record3);

        CryptoCurrencyService cryptoCurrencyService = new CryptoCurrencyService(cryptoCurrencyRepository);
        var result  = cryptoCurrencyService.getMaxPriceRecord(AllowedCurrency.BTC.toString());
        assertFalse(result == null);
        assertTrue(record1.getId().equals(result.get().getId()));
    }

    @Test
    void getMinPriceRecord() {
        var record1 = new CryptoCurrency("14",AllowedCurrency.BTC.toString(), 40000.0, LocalDateTime.now());
        var record2 = new CryptoCurrency("15",AllowedCurrency.BTC.toString(), 39000.0, LocalDateTime.now());
        var record3 = new CryptoCurrency("16",AllowedCurrency.BTC.toString(), 38000.0, LocalDateTime.now());
        cryptoCurrencyRepository.save(record1);
        cryptoCurrencyRepository.save(record2);
        cryptoCurrencyRepository.save(record3);

        CryptoCurrencyService cryptoCurrencyService = new CryptoCurrencyService(cryptoCurrencyRepository);
        var result  = cryptoCurrencyService.getMinPriceRecord(AllowedCurrency.BTC.toString());
        assertFalse(result == null);
        assertTrue(record3.getId().equals(result.get().getId()));
    }
}