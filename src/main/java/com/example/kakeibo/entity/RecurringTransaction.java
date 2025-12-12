package com.example.kakeibo.entity;

import com.example.kakeibo.enums.RecurrenceType;
import com.example.kakeibo.enums.TransactionType;
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
@Table(name = "recurring_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RecurringTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recurring_id")
    private Long recurringId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_type", nullable = false, length = 20)
    private RecurrenceType recurrenceType;

    @Column(name = "recurrence_day")
    private Integer recurrenceDay;  // 日付（月次:1-31, 週次:1-7）

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;  // NULLなら無期限

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "last_executed_date")
    private LocalDate lastExecutedDate;

    @Column(name = "is_group_shared", nullable = false)
    private Boolean isGroupShared = false;

    @Column(name = "is_split", nullable = false)
    private Boolean isSplit = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
