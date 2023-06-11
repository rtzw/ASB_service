package com.esp32.esp32Service.AuthorizedCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizedCardService {
    public final AuthorizedCardRepository authorizedCardRepository;

    @Autowired
    public AuthorizedCardService(AuthorizedCardRepository authorizedCardRepository) {
        this.authorizedCardRepository = authorizedCardRepository;
    }


    public void addNewCard(AuthorizedCard card) {
        if(authorizedCardRepository.getAuthorizedCardByCodeAndEspId(card.getCode(),card.getEspId()).isPresent())
            throw new IllegalArgumentException("Error!");
        authorizedCardRepository.save(card);
    }

    public Boolean isCardAuthorized(AuthorizedCard card) {
        return authorizedCardRepository.getAuthorizedCardByCodeAndEspId(card.getCode(), card.getEspId()).isPresent();
    }
}
