package com.example.kakeibo.dto;

import com.example.kakeibo.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class TransactionRequest {

    private Long transactionId;

    @NotNull(message = "カテゴリを選択してください")
    private Long categoryId;

    @NotNull(message = "取引タイプを選択してください")
    private TransactionType transactionType;

    @NotNull(message = "金額を入力してください")
    @Positive(message = "金額は正の値で入力してください")
    private BigDecimal amount;

    @NotNull(message = "日付を選択してください")
    private LocalDate transactionDate;

    private String description;

    private Long groupId;

    private Boolean isGroupShared = false;

    private Boolean isSplit = false;

    private Map<Long, BigDecimal> splitRatios;  // userId -> ratio
}
