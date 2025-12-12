package com.example.kakeibo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "category_id", "target_month"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Long budgetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;  // NULL=全体予算

    @Column(name = "target_month", nullable = false)
    private LocalDate targetMonth;  // yyyy-MM-01形式

    @Column(name = "budget_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal budgetAmount;

    @Column(name = "alert_threshold", precision = 5, scale = 2)
    private BigDecimal alertThreshold = new BigDecimal("80.00");  // デフォルト80%

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
