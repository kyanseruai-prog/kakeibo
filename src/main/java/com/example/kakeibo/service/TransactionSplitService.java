package com.example.kakeibo.service;

import com.example.kakeibo.entity.*;
import com.example.kakeibo.repository.GroupMemberRepository;
import com.example.kakeibo.repository.SplitRuleRepository;
import com.example.kakeibo.repository.TransactionSplitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionSplitService {

    @Autowired
    private TransactionSplitRepository transactionSplitRepository;

    @Autowired
    private SplitRuleRepository splitRuleRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    public void createSplits(Transaction transaction, Map<Long, BigDecimal> customRatios) {
        Group group = transaction.getGroup();
        List<GroupMember> members = groupMemberRepository.findByGroup(group);

        if (members.isEmpty()) {
            throw new RuntimeException("グループにメンバーがいません");
        }

        // 使用する比率を決定
        Map<Long, BigDecimal> ratios;
        if (customRatios != null && !customRatios.isEmpty()) {
            ratios = customRatios;
        } else {
            // デフォルト比率を取得（なければ均等割）
            List<SplitRule> rules = splitRuleRepository.findByGroup(group);
            if (!rules.isEmpty()) {
                ratios = rules.stream()
                    .collect(Collectors.toMap(
                        r -> r.getUser().getUserId(),
                        SplitRule::getSplitRatio
                    ));
            } else {
                // 均等割
                BigDecimal equalRatio = new BigDecimal("100")
                    .divide(new BigDecimal(members.size()), 2, RoundingMode.DOWN);
                ratios = members.stream()
                    .collect(Collectors.toMap(
                        m -> m.getUser().getUserId(),
                        m -> equalRatio
                    ));
            }
        }

        // 比率合計が100%か検証
        BigDecimal totalRatio = ratios.values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalRatio.compareTo(new BigDecimal("100")) != 0) {
            throw new RuntimeException("割り勘比率の合計は100%である必要があります");
        }

        // 各メンバーの負担額を計算
        BigDecimal totalAmount = transaction.getAmount();
        BigDecimal allocatedAmount = BigDecimal.ZERO;
        List<TransactionSplit> splits = new ArrayList<>();

        for (int i = 0; i < members.size(); i++) {
            GroupMember member = members.get(i);
            BigDecimal ratio = ratios.getOrDefault(member.getUser().getUserId(), BigDecimal.ZERO);
            BigDecimal splitAmount;

            // 最後のメンバーは端数調整
            if (i == members.size() - 1) {
                splitAmount = totalAmount.subtract(allocatedAmount);
            } else {
                splitAmount = totalAmount
                    .multiply(ratio)
                    .divide(new BigDecimal("100"), 2, RoundingMode.DOWN);
                allocatedAmount = allocatedAmount.add(splitAmount);
            }

            TransactionSplit split = new TransactionSplit();
            split.setTransaction(transaction);
            split.setUser(member.getUser());
            split.setSplitAmount(splitAmount);
            split.setSplitRatio(ratio);
            splits.add(split);
        }

        transactionSplitRepository.saveAll(splits);
    }

    public List<TransactionSplit> getSplitsByTransaction(Transaction transaction) {
        return transactionSplitRepository.findByTransaction(transaction);
    }
}
