package com.mongo.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cryptocurrencies")
public class CryptoCurrency {
    @Id
    private String id;
    private String code;
    private double price;
    private LocalDateTime createdAt;
}
