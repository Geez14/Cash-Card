package com.geez14.app.repo;

import com.geez14.app.entities.CashCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CashCardRepository extends CrudRepository<CashCard, Long>, PagingAndSortingRepository<CashCard, Long> {
    @Query("SELECT * FROM CASH_CARD WHERE ID=:cardNumber AND OWNER=:owner")
    Optional<CashCard> findByIdAndOwner(Long cardNumber, String owner);

    Page<CashCard> findAllByOwnerIgnoreCase(String owner, PageRequest pageRequest);

    boolean existsByIdAndOwner(Long requestedId, String name);
}
