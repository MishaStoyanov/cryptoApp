package com.integrio.cryptoapp.repositories;

import com.integrio.cryptoapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    void deleteByChatId(Long chatId);
}
