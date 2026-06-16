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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void shouldReturnCashcardWhenValidId() {
        var inputId = UUID.randomUUID();

        var savedCashcard = new CashcardModel(inputId, new BigDecimal("100"));

        when(cashcardRepository.findById(inputId)).thenReturn(Optional.of(savedCashcard));

        var response = cashcardService.findById(inputId);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(inputId);
        assertThat(response.amount()).isEqualByComparingTo(new BigDecimal("100"));

        verify(cashcardRepository).findById(inputId);
    }

    @Test
    void shouldThrowExceptionWhenInvalidId() {
        var inputId = UUID.randomUUID();

        when(cashcardRepository.findById(inputId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cashcardService.findById(inputId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cashcard não encontrado com o ID: " + inputId);

        verify(cashcardRepository).findById(inputId);
    }
}