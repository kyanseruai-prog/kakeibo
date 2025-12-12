package com.example.kakeibo.repository;

import com.example.kakeibo.entity.Category;
import com.example.kakeibo.entity.User;
import com.example.kakeibo.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // システムカテゴリを取得
    List<Category> findByIsSystemTrue();

    // ユーザーのカテゴリを取得（システムカテゴリ含む）
    List<Category> findByUserOrIsSystemTrue(User user);

    // カテゴリタイプで絞り込み
    List<Category> findByUserAndCategoryType(User user, TransactionType type);

    List<Category> findByIsSystemTrueAndCategoryType(TransactionType type);
}
