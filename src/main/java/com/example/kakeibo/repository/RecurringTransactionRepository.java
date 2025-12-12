package com.example.kakeibo.repository;

import com.example.kakeibo.entity.RecurringTransaction;
import com.example.kakeibo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {

    // ユーザーのアクティブな定期取引
    List<RecurringTransaction> findByUserAndIsActiveTrue(User user);

    // 実行すべき定期取引を検索
    @Query("SELECT rt FROM RecurringTransaction rt WHERE rt.isActive = true " +
           "AND rt.startDate <= :today " +
           "AND (rt.endDate IS NULL OR rt.endDate >= :today)")
    List<RecurringTransaction> findDueTransactions(@Param("today") LocalDate today);

    // ユーザーの全定期取引
    List<RecurringTransaction> findByUserOrderByCreatedAtDesc(User user);
}
