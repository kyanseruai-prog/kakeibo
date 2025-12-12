package com.example.kakeibo.repository;

import com.example.kakeibo.entity.Group;
import com.example.kakeibo.entity.GroupMember;
import com.example.kakeibo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    // グループのメンバー一覧
    List<GroupMember> findByGroup(Group group);

    // ユーザーのグループメンバーシップ
    List<GroupMember> findByUser(User user);

    // 特定のグループ・ユーザー組み合わせ
    Optional<GroupMember> findByGroupAndUser(Group group, User user);

    // メンバーかどうか確認
    boolean existsByGroupAndUser(Group group, User user);

    // グループのメンバー数
    long countByGroup(Group group);
}
