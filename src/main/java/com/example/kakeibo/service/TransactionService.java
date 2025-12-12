package com.example.kakeibo.service;

import com.example.kakeibo.dto.TransactionRequest;
import com.example.kakeibo.entity.Category;
import com.example.kakeibo.entity.Group;
import com.example.kakeibo.entity.Transaction;
import com.example.kakeibo.entity.User;
import com.example.kakeibo.enums.TransactionType;
import com.example.kakeibo.repository.CategoryRepository;
import com.example.kakeibo.repository.GroupRepository;
import com.example.kakeibo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TransactionSplitService transactionSplitService;

    public Transaction createTransaction(TransactionRequest request, User user) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new RuntimeException("カテゴリが見つかりません"));
        transaction.setCategory(category);

        transaction.setTransactionType(request.getTransactionType());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setDescription(request.getDescription());
        transaction.setIsGroupShared(request.getIsGroupShared() != null ? request.getIsGroupShared() : false);

        // グループ取引の場合
        if (request.getGroupId() != null) {
            Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("グループが見つかりません"));
            transaction.setGroup(group);
            transaction.setIsGroupShared(true);

            // 割り勘処理
            if (request.getIsSplit() != null && request.getIsSplit()) {
                transaction.setIsSplit(true);
            }
        }

        Transaction saved = transactionRepository.save(transaction);

        // 割り勘の場合は割り勘明細を作成
        if (saved.getIsSplit() && saved.getGroup() != null) {
            transactionSplitService.createSplits(saved, request.getSplitRatios());
        }

        return saved;
    }

    public List<Transaction> getTransactionsByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByUserOrGroupMemberAndDateBetween(user, startDate, endDate);
    }

    public Transaction findById(Long transactionId) {
        return transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("取引が見つかりません"));
    }

    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public Map<String, BigDecimal> getMonthlySummary(User user, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<Transaction> transactions = transactionRepository
            .findByUserAndTransactionDateBetweenOrderByTransactionDateDesc(user, startDate, endDate);

        BigDecimal totalIncome = transactions.stream()
            .filter(t -> t.getTransactionType() == TransactionType.INCOME)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
            .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Map.of(
            "income", totalIncome,
            "expense", totalExpense,
            "balance", totalIncome.subtract(totalExpense)
        );
    }

    public Map<String, BigDecimal> getCategoryExpensesByMonth(User user, int year, int month) {
        List<Object[]> results = transactionRepository
            .sumByUserAndTypeAndMonthGroupByCategory(user, TransactionType.EXPENSE, year, month);

        return results.stream()
            .collect(Collectors.toMap(
                r -> ((Category) r[0]).getCategoryName(),
                r -> (BigDecimal) r[1]
            ));
    }
}
