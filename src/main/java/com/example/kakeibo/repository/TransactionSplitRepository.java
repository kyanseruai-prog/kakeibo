package com.example.kakeibo.repository;

import com.example.kakeibo.entity.Transaction;
import com.example.kakeibo.entity.TransactionSplit;
import com.example.kakeibo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionSplitRepository extends JpaRepository<TransactionSplit, Long> {

    // 取引の割り勘明細
    List<TransactionSplit> findByTransaction(Transaction transaction);

    // ユーザーの割り勘明細
    List<TransactionSplit> findByUser(User user);

    // 取引の割り勘明細を削除
    void deleteByTransaction(Transaction transaction);
}
