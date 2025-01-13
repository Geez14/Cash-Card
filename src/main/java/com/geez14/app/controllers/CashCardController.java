package com.geez14.app.controllers;

import com.geez14.app.entities.CashCard;
import com.geez14.app.repo.CashCardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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
        // return cashCard.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
        // this is my code!
        if (cashCard.isPresent()) {
            return ResponseEntity.ok(cashCard.get());
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * CRUD {
     * Operation: Write,
     * EndPoint: /cashcards/,
     * Body: {amount: xxx.xx},
     * HTTPMethod: POST,
     * }
     *
     * @param cashCard com.geez14.app.entities.CashCard
     * @param ucb      org.springframework.web.util.UriComponentBuilder
     * @return java.lang.Void
     * Post mapping on /cashcards for creating new CashCard
     * UriComponent was injected from our Spring IoC container.
     * Because the official specification continues to state the following:
     * > Send a 201 (created) response <b>containing a Location header field</b> that
     * provides an identifier for the primary resource created
     */
    @PostMapping
    private ResponseEntity<Void> create(@RequestBody CashCard cashCard, UriComponentsBuilder ucb) {
        CashCard savedCashCard = cashCardRepository.save(cashCard);
        URI locator = ucb
                .path("/cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();
        return ResponseEntity.created(locator).build();
    }

//    @GetMapping
//    private ResponseEntity<Iterable<CashCard>> findAll() {
//        return ResponseEntity.ok(cashCardRepository.findAll());
//    }

    @GetMapping
    private ResponseEntity<Iterable<CashCard>> findAll(Pageable pageable) {
        Page<CashCard> page = cashCardRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "amount"))
                ));
        return ResponseEntity.ok(page.getContent());
    }
}
