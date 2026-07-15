package com.gabriel.cashcard_api.repositories;

import com.gabriel.cashcard_api.entities.Cashcard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CashcardRepository extends CrudRepository<Cashcard, UUID>, PagingAndSortingRepository<Cashcard, UUID> {
}
