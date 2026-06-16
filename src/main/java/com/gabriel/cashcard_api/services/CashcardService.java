package com.gabriel.cashcard_api.services;

import com.gabriel.cashcard_api.dtos.CashcardRequest;
import com.gabriel.cashcard_api.dtos.CashcardResponse;
import com.gabriel.cashcard_api.models.CashcardModel;
import com.gabriel.cashcard_api.repositories.CashcardRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("Cashcard não encontrado com o ID: " + id));
    }
}
