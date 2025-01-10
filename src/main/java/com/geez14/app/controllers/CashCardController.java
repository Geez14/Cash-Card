package com.geez14.app.controllers;

import com.geez14.app.entities.CashCard;
import com.geez14.app.repo.CashCardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("CashCardAPI")
@RequestMapping(path = "/cashcards")
public class CashCardController {
    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    /**
     * CRUD {
     * Operation: Read,
     * EndPoint: /cashcards/{id}
     * HTTPMethod: GET
     * }
     *
     * @param requestedId java.lang.Long
     * @return com.geez14.app.entities.CashCard
     */
    @GetMapping(path = "/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        java.util.Optional<CashCard> cashCard = cashCardRepository.findById(requestedId);
        // ResponseEntity.ok(cashCard.orElse(null)); fail test case
        // the below code is written by AI not me
        // return cashCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        // this is my code!
        if (cashCard.isPresent()) {
            return ResponseEntity.ok(cashCard.get());
        }
        return ResponseEntity.notFound().build();
    }
}
