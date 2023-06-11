package com.esp32.esp32Service.AuthorizedCard;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizedCardRepository extends JpaRepository<AuthorizedCard, Long> {
    @Query(value = "select au from AuthorizedCard au where au.code = :code and au.espId = :espId")
    Optional<AuthorizedCard> getAuthorizedCardByCodeAndEspId(String code, String espId);
}
