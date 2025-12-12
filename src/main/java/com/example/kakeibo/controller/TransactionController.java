package com.example.kakeibo.controller;

import com.example.kakeibo.dto.TransactionRequest;
import com.example.kakeibo.entity.User;
import com.example.kakeibo.enums.TransactionType;
import com.example.kakeibo.service.CategoryService;
import com.example.kakeibo.service.GroupService;
import com.example.kakeibo.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String listTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal User user,
            Model model) {

        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        model.addAttribute("transactions", transactionService.getTransactionsByDateRange(user, startDate, endDate));
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "transaction/list";
    }

    @GetMapping("/new")
    public String newTransactionForm(@AuthenticationPrincipal User user, Model model) {
        TransactionRequest request = new TransactionRequest();
        request.setTransactionDate(LocalDate.now());
        request.setTransactionType(TransactionType.EXPENSE);

        model.addAttribute("transaction", request);
        model.addAttribute("categories", categoryService.getUserCategories(user));
        model.addAttribute("groups", groupService.getUserGroups(user));

        return "transaction/form";
    }

    @PostMapping
    public String createTransaction(
            @Valid @ModelAttribute("transaction") TransactionRequest request,
            BindingResult result,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getUserCategories(user));
            model.addAttribute("groups", groupService.getUserGroups(user));
            return "transaction/form";
        }

        try {
            transactionService.createTransaction(request, user);
            redirectAttributes.addFlashAttribute("message", "取引を登録しました");
            return "redirect:/transactions";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryService.getUserCategories(user));
            model.addAttribute("groups", groupService.getUserGroups(user));
            return "transaction/form";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteTransaction(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            transactionService.deleteTransaction(id);
            redirectAttributes.addFlashAttribute("message", "取引を削除しました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/transactions";
    }
}
