package com.geoly.app.repositories;

import com.geoly.app.models.Token;
import com.geoly.app.models.TokenType;
import com.geoly.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByTokenAndAction(String token, TokenType tokenType);

    Optional<Token> findByUserAndAction(User user, TokenType tokenType);
}
