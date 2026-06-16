package com.gabriel.cashcard_api.controllers;

import com.gabriel.cashcard_api.dtos.CashcardRequest;
import com.gabriel.cashcard_api.dtos.CashcardResponse;
import com.gabriel.cashcard_api.services.CashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/cashcards")
public class CashcardController {
    private final CashcardService cashcardService;

    private CashcardController(CashcardService cashcardService) {
        this.cashcardService = cashcardService;
    }

    @PostMapping()
    private ResponseEntity<CashcardResponse> createCashcard(@RequestBody CashcardRequest cashcardRequest) {
        var cashcardResponse = cashcardService.createCashcard(cashcardRequest);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cashcardResponse.id())
                .toUri();

        return ResponseEntity.created(location).body(cashcardResponse);
    }
}
