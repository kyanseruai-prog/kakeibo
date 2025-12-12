package com.example.kakeibo.repository;

import com.example.kakeibo.entity.Group;
import com.example.kakeibo.entity.GroupInvitation;
import com.example.kakeibo.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, Long> {

    // トークンで検索
    Optional<GroupInvitation> findByInvitationToken(String token);

    // メールアドレスとステータスで検索
    List<GroupInvitation> findByInvitedEmailAndStatus(
        String email, InvitationStatus status);

    // 既存の保留中招待をチェック
    boolean existsByGroupAndInvitedEmailAndStatus(
        Group group, String email, InvitationStatus status);

    // グループの招待一覧
    List<GroupInvitation> findByGroupOrderByInvitedAtDesc(Group group);

    // 期限切れの招待を検索
    List<GroupInvitation> findByStatusAndExpiresAtBefore(
        InvitationStatus status, LocalDateTime dateTime);
}
