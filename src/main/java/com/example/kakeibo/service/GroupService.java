package com.example.kakeibo.service;

import com.example.kakeibo.entity.Group;
import com.example.kakeibo.entity.GroupMember;
import com.example.kakeibo.entity.User;
import com.example.kakeibo.repository.GroupMemberRepository;
import com.example.kakeibo.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    public Group createGroup(String groupName, String description, User createdBy) {
        Group group = new Group();
        group.setGroupName(groupName);
        group.setDescription(description);
        group.setCreatedBy(createdBy);

        Group saved = groupRepository.save(group);

        // 作成者を自動的にメンバーに追加
        addMember(saved, createdBy);

        return saved;
    }

    public void addMember(Group group, User user) {
        if (!groupMemberRepository.existsByGroupAndUser(group, user)) {
            GroupMember member = new GroupMember();
            member.setGroup(group);
            member.setUser(user);
            groupMemberRepository.save(member);
        }
    }

    public List<Group> getUserGroups(User user) {
        return groupRepository.findByMember(user);
    }

    public Group findById(Long groupId) {
        return groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("グループが見つかりません"));
    }

    public boolean isMember(Group group, User user) {
        return groupMemberRepository.existsByGroupAndUser(group, user);
    }

    public List<GroupMember> getGroupMembers(Group group) {
        return groupMemberRepository.findByGroup(group);
    }
}
