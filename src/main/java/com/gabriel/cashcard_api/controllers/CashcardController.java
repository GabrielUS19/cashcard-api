package com.gabriel.cashcard_api.controllers;

import com.gabriel.cashcard_api.dto.requests.CashcardRequest;
import com.gabriel.cashcard_api.dto.responses.CashcardResponse;
import com.gabriel.cashcard_api.services.CashcardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

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

    @GetMapping("/{id}")
    private ResponseEntity<CashcardResponse> findById(@PathVariable("id") UUID id) {
        var cashcardResponse = cashcardService.findById(id);
        return ResponseEntity.ok(cashcardResponse);
    }

    @GetMapping
    private ResponseEntity<Page<CashcardResponse>> findAll(
            @PageableDefault(page = 0, size = 10, sort = "amount", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(cashcardService.findAll(pageable));
    }
}
