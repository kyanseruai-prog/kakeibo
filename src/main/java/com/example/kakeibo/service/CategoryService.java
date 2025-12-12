package com.example.kakeibo.service;

import com.example.kakeibo.entity.Category;
import com.example.kakeibo.entity.User;
import com.example.kakeibo.enums.TransactionType;
import com.example.kakeibo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getUserCategories(User user) {
        // ユーザー固有のカテゴリとシステムカテゴリを両方取得
        return categoryRepository.findByUserOrIsSystemTrue(user);
    }

    public List<Category> getCategoriesByType(User user, TransactionType type) {
        List<Category> userCategories = categoryRepository.findByUserAndCategoryType(user, type);
        List<Category> systemCategories = categoryRepository.findByIsSystemTrueAndCategoryType(type);

        return Stream.concat(userCategories.stream(), systemCategories.stream())
            .collect(Collectors.toList());
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("カテゴリが見つかりません"));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
