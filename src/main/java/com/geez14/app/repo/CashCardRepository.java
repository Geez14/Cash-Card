package com.geez14.app.repo;

import com.geez14.app.entities.CashCard;
import jdk.jfr.BooleanFlag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CashCardRepository extends CrudRepository<CashCard, Long>, PagingAndSortingRepository<CashCard, Long> {
    @Query("SELECT * FROM CASH_CARD WHERE ID=:cardNumber AND OWNER=:owner")
    Optional<CashCard> findByIdAndOwner(Long cardNumber, String owner);

    Page<CashCard> findAllByOwnerIgnoreCase(String owner, PageRequest pageRequest);

    boolean existsByIdAndOwner(Long requestedId, String name);

    @Query("SELECT ID FROM CASH_CARD WHERE ID IN (:data) AND OWNER=:owner")
    List<Long> getIdsByRequestedIdsAndOwner(Iterable<Long> data, String owner);
}
