package com.example.kakeibo.entity;

import com.example.kakeibo.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_invitations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long invitationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(name = "invited_email", nullable = false)
    private String invitedEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by", nullable = false)
    private User invitedBy;

    @Column(name = "invitation_token", unique = true, nullable = false)
    private String invitationToken;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private InvitationStatus status = InvitationStatus.PENDING;

    @Column(name = "invited_at")
    private LocalDateTime invitedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;
}
