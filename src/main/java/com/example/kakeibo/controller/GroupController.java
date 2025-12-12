package com.example.kakeibo.controller;

import com.example.kakeibo.entity.Group;
import com.example.kakeibo.entity.User;
import com.example.kakeibo.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String listGroups(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("groups", groupService.getUserGroups(user));
        return "group/list";
    }

    @GetMapping("/new")
    public String newGroupForm() {
        return "group/form";
    }

    @PostMapping
    public String createGroup(
            @RequestParam String groupName,
            @RequestParam(required = false) String description,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {

        try {
            Group group = groupService.createGroup(groupName, description, user);
            redirectAttributes.addFlashAttribute("message", "グループを作成しました");
            return "redirect:/groups/" + group.getGroupId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/groups/new";
        }
    }

    @GetMapping("/{id}")
    public String groupDetail(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        Group group = groupService.findById(id);

        if (!groupService.isMember(group, user)) {
            return "redirect:/groups?error=unauthorized";
        }

        model.addAttribute("group", group);
        model.addAttribute("members", groupService.getGroupMembers(group));

        return "group/detail";
    }
}
