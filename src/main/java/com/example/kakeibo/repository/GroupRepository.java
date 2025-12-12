package com.example.kakeibo.repository;

import com.example.kakeibo.entity.Group;
import com.example.kakeibo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    // ユーザーが作成したグループ
    List<Group> findByCreatedBy(User user);

    // ユーザーが所属するグループ（メンバーとして）
    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.user = :user")
    List<Group> findByMember(@Param("user") User user);
}
