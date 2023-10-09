package com.integrio.cryptoapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PriceChangeScheduler {
    private final PriceChangeService priceChangeService;
    private final CurrencyService currencyService;

    @Scheduled(fixedRate = 10000)//change to set time to update
    public void checkPriceChangesScheduled() {
        priceChangeService.checkPriceChanges(currencyService.getCurrentCurrencyList());
    }
}