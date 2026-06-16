package com.gabriel.cashcard_api.services;

import com.gabriel.cashcard_api.dtos.CashcardRequest;
import com.gabriel.cashcard_api.dtos.CashcardResponse;
import com.gabriel.cashcard_api.models.CashcardModel;
import com.gabriel.cashcard_api.repositories.CashcardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CashcardServiceTest {
    @Mock
    private CashcardRepository cashcardRepository;

    @InjectMocks
    private CashcardService cashcardService;

    @Test
    void shouldCreateCashcardSuccessfully() {
        var inputCashcard = new CashcardRequest(new BigDecimal("100.0"));

        UUID generatedId = UUID.randomUUID();
        CashcardModel savedCashcard = new CashcardModel(generatedId, new BigDecimal("100.0"));

        when(cashcardRepository.save(any(CashcardModel.class))).thenReturn(savedCashcard);

        CashcardResponse result = cashcardService.createCashcard(inputCashcard);

        assertThat(result).isNotNull();

        assertThat(result.id()).isEqualTo(generatedId);

        assertThat(result.amount()).isEqualByComparingTo(inputCashcard.amount());

        verify(cashcardRepository).save(any(CashcardModel.class));
    }

}