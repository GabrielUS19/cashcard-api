package com.gabriel.cashcard_api.repositories;

import com.gabriel.cashcard_api.models.CashcardModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CashcardRepository extends CrudRepository<CashcardModel, UUID>, PagingAndSortingRepository<CashcardModel, UUID> {
}
