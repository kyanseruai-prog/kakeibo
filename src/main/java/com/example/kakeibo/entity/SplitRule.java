package com.example.kakeibo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "split_rules", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"group_id", "user_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SplitRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "split_rule_id")
    private Long splitRuleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "split_ratio", nullable = false, precision = 5, scale = 2)
    private BigDecimal splitRatio;  // 割り勘比率（%）

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
