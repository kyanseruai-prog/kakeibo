package com.example.kakeibo.repository;

import com.example.kakeibo.entity.Category;
import com.example.kakeibo.entity.Group;
import com.example.kakeibo.entity.Transaction;
import com.example.kakeibo.entity.User;
import com.example.kakeibo.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ユーザーの取引を日付範囲で検索
    List<Transaction> findByUserAndTransactionDateBetweenOrderByTransactionDateDesc(
        User user, LocalDate startDate, LocalDate endDate);

    // グループの取引を日付範囲で検索
    List<Transaction> findByGroupAndTransactionDateBetweenOrderByTransactionDateDesc(
        Group group, LocalDate startDate, LocalDate endDate);

    // ユーザーまたはグループの取引を日付範囲で検索
    @Query("SELECT t FROM Transaction t WHERE (t.user = :user OR t.group IN " +
           "(SELECT gm.group FROM GroupMember gm WHERE gm.user = :user)) " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserOrGroupMemberAndDateBetween(
        @Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    // カテゴリ別月次合計
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user = :user AND t.category = :category " +
           "AND t.transactionType = 'EXPENSE' " +
           "AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month")
    BigDecimal sumExpenseByUserAndCategoryAndMonth(
        @Param("user") User user,
        @Param("category") Category category,
        @Param("year") int year,
        @Param("month") int month);

    // 月次合計（全カテゴリ）
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user = :user AND t.transactionType = :type " +
           "AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month")
    BigDecimal sumByUserAndTypeAndMonth(
        @Param("user") User user,
        @Param("type") TransactionType type,
        @Param("year") int year,
        @Param("month") int month);

    // カテゴリ別集計
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t " +
           "WHERE t.user = :user AND t.transactionType = :type " +
           "AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month " +
           "GROUP BY t.category")
    List<Object[]> sumByUserAndTypeAndMonthGroupByCategory(
        @Param("user") User user,
        @Param("type") TransactionType type,
        @Param("year") int year,
        @Param("month") int month);
}
