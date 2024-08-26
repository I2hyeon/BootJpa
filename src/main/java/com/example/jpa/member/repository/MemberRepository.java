package com.example.jpa.member.repository;

import com.example.jpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> { // 엔티티, ID타입
}
