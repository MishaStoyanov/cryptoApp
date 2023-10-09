package com.integrio.cryptoapp.repositories;

import com.integrio.cryptoapp.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    void deleteAllByChatId(Long chatId);
}
