package com.integrio.cryptoapp.services;

import com.integrio.cryptoapp.models.Currency;
import com.integrio.cryptoapp.repositories.CurrencyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@AllArgsConstructor
@Slf4j
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private static final String WEBSITE_URL ="https://api.mexc.com/api/v3/ticker/price";

    public void setCurrencyRate(Long chatId) {
        List<Currency> currencyList = new ArrayList<>();
        try {
            URI url = new URI(WEBSITE_URL);
            Scanner scanner = new Scanner((InputStream) url.toURL().getContent());
            StringBuilder result = new StringBuilder();

            while (scanner.hasNext()) {
                result.append(scanner.nextLine());
            }
            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                var currency = new Currency();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                currency.setSymbol(jsonObject.getString("symbol"));
                currency.setPrice(jsonObject.getDouble("price"));
                currency.setChatId(chatId);
                currencyList.add(currency);
            }
            currencyRepository.saveAll(currencyList);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public List<Currency> getCurrentCurrencyList() {
        List<Currency> currencyList = new ArrayList<>();
        try {
            URI url = new URI(WEBSITE_URL);
            Scanner scanner = new Scanner((InputStream) url.toURL().getContent());
            StringBuilder result = new StringBuilder();

            while (scanner.hasNext()) {
                result.append(scanner.nextLine());
            }
            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                var currency = new Currency();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                currency.setSymbol(jsonObject.getString("symbol"));
                currency.setPrice(jsonObject.getDouble("price"));
                currency.setChatId(null);
                currencyList.add(currency);
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return currencyList;
    }
}