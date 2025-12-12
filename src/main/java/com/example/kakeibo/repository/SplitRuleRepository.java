package com.example.kakeibo.repository;

import com.example.kakeibo.entity.Group;
import com.example.kakeibo.entity.SplitRule;
import com.example.kakeibo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SplitRuleRepository extends JpaRepository<SplitRule, Long> {

    // グループの割り勘ルール一覧
    List<SplitRule> findByGroup(Group group);

    // グループ・ユーザーの割り勘ルール
    Optional<SplitRule> findByGroupAndUser(Group group, User user);

    // グループの割り勘ルールを削除
    void deleteByGroup(Group group);
}
