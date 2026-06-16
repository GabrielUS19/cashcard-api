package com.gabriel.cashcard_api.services;

import com.gabriel.cashcard_api.dtos.CashcardRequest;
import com.gabriel.cashcard_api.dtos.CashcardResponse;
import com.gabriel.cashcard_api.models.CashcardModel;
import com.gabriel.cashcard_api.repositories.CashcardRepository;
import org.springframework.stereotype.Service;

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
}
