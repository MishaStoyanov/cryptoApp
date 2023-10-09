package com.integrio.cryptoapp.bot;

import com.integrio.cryptoapp.config.BotConfig;
import com.integrio.cryptoapp.models.User;
import com.integrio.cryptoapp.repositories.CurrencyRepository;
import com.integrio.cryptoapp.repositories.UserRepository;
import com.integrio.cryptoapp.services.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

import static com.integrio.cryptoapp.Constants.MIN_PERCENT_CHANGE;
import static com.integrio.cryptoapp.Constants.MAX_USERS_COUNT;

@Component
@AllArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    @Transactional
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> {
                    var count = userRepository.count();
                    if (count > MAX_USERS_COUNT) {
                        sendMessage(chatId, "Sorry, but we are out of space to track the current market position. Try again later");
                        break;
                    }
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                }
                case "/stop" -> endCommandReceived(chatId);

                default ->
                    sendMessage(chatId, "Sorry, something wend wrong. Try again later :(");
            }
        }

    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n" +
                "We upload all actual prices for most famous cryptocurrency. If they course changed by more than " + MIN_PERCENT_CHANGE + "% we will inform you!" +
                "Data is updated every 10 seconds.";
        sendMessage(chatId, answer);
        currencyService.setCurrencyRate(chatId);
        var user = new User();
        user.setName(name);
        user.setCreationDate(LocalDateTime.now());
        user.setChatId(chatId);
        userRepository.save(user);
    }

    @Transactional
    public void endCommandReceived(Long chatId) {
        sendMessage(chatId, "Thanks for using. See you later!");
        userRepository.deleteByChatId(chatId);
        currencyRepository.deleteAllByChatId(chatId);
    }

    public void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}