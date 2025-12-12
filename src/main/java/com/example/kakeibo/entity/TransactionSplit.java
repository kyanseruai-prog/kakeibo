package com.example.kakeibo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_splits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TransactionSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "split_id")
    private Long splitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "split_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal splitAmount;

    @Column(name = "split_ratio", nullable = false, precision = 5, scale = 2)
    private BigDecimal splitRatio;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
