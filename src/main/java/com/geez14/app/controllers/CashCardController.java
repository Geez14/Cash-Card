package com.geez14.app.controllers;

import com.geez14.app.entities.CashCard;
import com.geez14.app.repo.CashCardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController("CashCardAPI")
@RequestMapping(path = "/cashcards")
public class CashCardController {
    private final CashCardRepository cashCardRepository;
    private final CompositeUriComponentsContributor compositeUriComponentsContributor;

    private CashCardController(CashCardRepository cashCardRepository, CompositeUriComponentsContributor compositeUriComponentsContributor) {
        this.cashCardRepository = cashCardRepository;
        this.compositeUriComponentsContributor = compositeUriComponentsContributor;
    }

    /**
     * CRUD {
     * Operation: Read,
     * EndPoint: /cashcards/{id}
     * HTTPMethod: GET
     * }
     *
     * @param requestedId java.lang.Long
     * @return org.springframework.http.ResponseEntity
     */
    @GetMapping(path = "/{requestedId}")
    private ResponseEntity<CashCard> getCashCard(@PathVariable Long requestedId, Principal principal) {
        Optional<CashCard> cashCard = cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
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
     * <p>
     * Post mapping on /cashcards for creating new CashCard
     * UriComponent was injected from our Spring IoC container.
     * Because the official specification continues to state the following:
     * </p>
     * <p>
     * Send a 201 (created) response <b>containing a Location header field</b> that
     * provides an identifier for the primary resource created
     * </p>
     * <br/>
     * <code>
     * CRUD {
     * Operation: Write,
     * EndPoint: /cashcards/,
     * Body: {amount: xxx.xx},
     * HTTPMethod: POST,
     * }
     * </code>
     *
     * @param newCashCard com.geez14.app.entities.CashCard
     * @param ucb         org.springframework.web.util.UriComponentBuilder
     * @return org.springframework.http.ResponseEntity
     */
    @PostMapping
    private ResponseEntity<Void> createNewCashCard(@RequestBody CashCard newCashCard, UriComponentsBuilder ucb, Principal principal) {
        CashCard cashCardWithOwner = new CashCard(null, newCashCard.amount(), principal.getName());
        CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);
        URI locator = ucb.path("/cashcards/{id}").buildAndExpand(savedCashCard.id()).toUri();
        return ResponseEntity.created(locator).build();
    }

    /**
     * @param pageable  org.springframework.data.domain.Pageable
     * @param principal java.security.Principal
     * @return org.springframework.http.ResponseEntity
     */
    @GetMapping
    private ResponseEntity<Iterable<CashCard>> getCashCards(Pageable pageable, Principal principal) {
        Page<CashCard> page = cashCardRepository.findAllByOwnerIgnoreCase(principal.getName(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))));
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard newCashCard, Principal principal) {
        // This doesn't solve the issue, if the card doesn't exist to return 404 error!
        // CashCard cashCardWithOwner = new CashCard(requestedId, newCashCard.amount(), principal.getName());
        // cashCardRepository.save(cashCardWithOwner);
        if (cashCardRepository.existsByIdAndOwner(requestedId, principal.getName())) {
            cashCardRepository.save(new CashCard(requestedId, newCashCard.amount(), principal.getName()));
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes cashCard, Just check weather the card exist or not? then delete it if exist!
     *
     * @param requestedId java.lang.Long
     * @param principal   java.security.Principal
     * @return org.springframework.http.ResponseEntity
     */
    @DeleteMapping(path = "/{requestedId}")
    private ResponseEntity<Void> deleteCard(@PathVariable Long requestedId, Principal principal) {
        if (cashCardRepository.existsByIdAndOwner(requestedId, principal.getName())) {
            cashCardRepository.deleteById(requestedId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    private ResponseEntity<Iterable<Long>> deleteSelectedCashCards(@RequestBody Iterable<Long> data, Principal principal) {
        List<Long> validData = cashCardRepository.getIdsByRequestedIdsAndOwner(data, principal.getName());
        if (validData.isEmpty())
            return ResponseEntity.notFound().build();
        cashCardRepository.deleteAllById(validData);
        return ResponseEntity.ok(validData);
    }
}
