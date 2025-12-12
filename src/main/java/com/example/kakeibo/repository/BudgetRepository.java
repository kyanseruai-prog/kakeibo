package com.example.kakeibo.repository;

import com.example.kakeibo.entity.Budget;
import com.example.kakeibo.entity.Category;
import com.example.kakeibo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // ユーザーの特定月の予算一覧
    List<Budget> findByUserAndTargetMonth(User user, LocalDate targetMonth);

    // カテゴリ別予算
    Optional<Budget> findByUserAndCategoryAndTargetMonth(
        User user, Category category, LocalDate targetMonth);

    // 全体予算（categoryがnull）
    Optional<Budget> findByUserAndCategoryIsNullAndTargetMonth(
        User user, LocalDate targetMonth);

    // ユーザーの全予算
    List<Budget> findByUserOrderByTargetMonthDesc(User user);
}
