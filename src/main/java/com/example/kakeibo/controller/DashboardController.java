package com.example.kakeibo.controller;

import com.example.kakeibo.entity.User;
import com.example.kakeibo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        // 月次サマリー取得
        Map<String, BigDecimal> summary = transactionService.getMonthlySummary(user, year, month);

        // カテゴリ別支出取得
        Map<String, BigDecimal> categoryExpenses = transactionService.getCategoryExpensesByMonth(user, year, month);

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("summary", summary);
        model.addAttribute("categoryExpenses", categoryExpenses);

        return "dashboard/index";
    }
}
