package com.integrio.cryptoapp.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    private Double price;
    private Long chatId;
}