package com.example.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberMemoDTO {

    // 멤버, 메모 엔티티의 필요한 값들을 명시
    private String id; // Member
    private String name; // Member
    private LocalDateTime signDate; // Member
    private long mno; // Memo
    private String writer; // Memo
    private String text; // Memo

}
