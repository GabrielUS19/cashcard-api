package com.gabriel.cashcard_api.services;

import com.gabriel.cashcard_api.dtos.CashcardRequest;
import com.gabriel.cashcard_api.dtos.CashcardResponse;
import com.gabriel.cashcard_api.models.CashcardModel;
import com.gabriel.cashcard_api.repositories.CashcardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
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
    @DisplayName("Should create a cash card successfully")
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
    @DisplayName("Should return a cash card when the user provides a valid ID")
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
    @DisplayName("Should throw a IllegalArgumentException when the user provides an invalid ID")
    void shouldThrowExceptionWhenInvalidId() {
        var inputId = UUID.randomUUID();

        when(cashcardRepository.findById(inputId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cashcardService.findById(inputId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cashcard não encontrado com o ID: " + inputId);

        verify(cashcardRepository).findById(inputId);
    }

    @Test
    @DisplayName("Should return a page with content when the user provides a valid page")
    void shouldReturnPageOfCashcards() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("amount").descending()
        );

        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var card1 = new CashcardModel(id1, new BigDecimal("200"));
        var card2 = new CashcardModel(id2, new BigDecimal("100"));

        List<CashcardModel> cashcardList = List.of(card1, card2);
        Page<CashcardModel> mockPage = new PageImpl<>(cashcardList);

        when(cashcardRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<CashcardResponse> response = cashcardService.findAll(pageable);

        assertThat(response.getContent())
                .extracting(CashcardResponse::id)
                .containsExactly(id1, id2);

        assertThat(response.getContent())
                .extracting(CashcardResponse::amount)
                .containsExactly(card1.getAmount(), card2.getAmount());

        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getTotalElements()).isEqualTo(2);
        assertThat(response.getSize()).isEqualTo(2);

        verify(cashcardRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return a empty page when index out of range")
    void shouldReturnEmptyContentWhenInvalidPage() {
        var pageable = PageRequest.of(1, 10, Sort.by("amount").descending());

        List<CashcardModel> emptyDatabaseResult = List.of();
        long totalElementsInDatabase = 2L;

        Page<CashcardModel> mockEmptyPage = new PageImpl<>(emptyDatabaseResult, pageable, totalElementsInDatabase);

        when(cashcardRepository.findAll(pageable)).thenReturn(mockEmptyPage);

        Page<CashcardResponse> response = cashcardService.findAll(pageable);

        assertThat(response).isNotNull();

        assertThat(response.getContent())
                .as("The content should be empty when index out of range")
                .isEmpty();

        assertThat(response.getTotalPages())
                .as("The total pages should be 1")
                .isEqualTo(1);

        assertThat(response.getTotalElements())
                .as("The total elements should be 2")
                .isEqualTo(2L);

        assertThat(response.getSize())
                .as("The page size should be 10")
                .isEqualTo(10);

        verify(cashcardRepository).findAll(pageable);
    }
}