package com.integrio.cryptoapp.services;

import com.integrio.cryptoapp.bot.TelegramBot;
import com.integrio.cryptoapp.models.Currency;
import com.integrio.cryptoapp.repositories.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

import static com.integrio.cryptoapp.Constants.MIN_PERCENT_CHANGE;

@Service
@RequiredArgsConstructor
public class PriceChangeService {
    private final CurrencyRepository currencyRepository;
    private final TelegramBot telegramBot;

    public void checkPriceChanges(List<Currency> currencyList) {
        List<Currency> currencies = currencyRepository.findAll();

        for (Currency currency : currencies) {
            double currentPrice = getCurrentPriceForSymbol(currencyList, currency.getSymbol());
            double savedPrice = currency.getPrice();
            double priceChangePercent = calculatePriceChangePercent(currentPrice, savedPrice);

            if (Math.abs(priceChangePercent) > MIN_PERCENT_CHANGE) {
                String message = "Price for " + currency.getSymbol() + " has changed by " + new DecimalFormat("0.000").format(priceChangePercent) + "%. Current price: " +
                        new DecimalFormat("0.00000000000000000000").format(currentPrice);

                telegramBot.sendMessage(currency.getChatId(), message);
           /*     currency.setPrice(currentPrice);
                currencyRepository.save(currency);*/
            }
        }
    }

    private double getCurrentPriceForSymbol(List<Currency> currencyList, String symbol) {
        return currencyList.stream()
                .filter(currency -> currency.getSymbol().equals(symbol))
                .toList().get(0).getPrice();
    }


    private double calculatePriceChangePercent(double currentPrice, double savedPrice) {
        if (savedPrice == 0) {
            return 0;
        }
        return ((currentPrice - savedPrice) / savedPrice) * 100;
    }
}
