package com.gabriel.cashcard_api.services;

import com.gabriel.cashcard_api.dto.CashcardRequest;
import com.gabriel.cashcard_api.dto.CashcardResponse;
import com.gabriel.cashcard_api.exceptions.CashcardNotFoundException;
import com.gabriel.cashcard_api.models.CashcardModel;
import com.gabriel.cashcard_api.repositories.CashcardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CashcardService {
    private final CashcardRepository cashcardRepository;

    public CashcardService(CashcardRepository cashcardRepository) {
        this.cashcardRepository = cashcardRepository;
    }

    public CashcardResponse createCashcard(CashcardRequest cashcardRequest) {
        var cashcardModel = new CashcardModel();
        cashcardModel.setAmount(cashcardRequest.amount());

        var savedCashcard = cashcardRepository.save(cashcardModel);

        return new CashcardResponse(savedCashcard.getId(), savedCashcard.getAmount());
    }

    public CashcardResponse findById(UUID id) {
        return cashcardRepository
                .findById(id)
                .map(model -> new CashcardResponse(model.getId(), model.getAmount()))
                .orElseThrow(() -> new CashcardNotFoundException("Cash Card not found with ID: " + id));
    }

    public Page<CashcardResponse> findAll(Pageable pageable) {
        var finalPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by("amount").descending())
        );

        return cashcardRepository.findAll(finalPageable)
                .map(cashcardModel -> new CashcardResponse(cashcardModel.getId(), cashcardModel.getAmount()));
    }
}
