package com.example.jpa;

import com.example.jpa.entity.Memo;
import com.example.jpa.memo.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
public class JPAPageTest02 {

    @Autowired
    MemoRepository memoRepository;

    // 기본정렬
    @Test
    public void testCode01() {

//        Sort.by("mno").ascending();
//        Sort sort = Sort.by("mno").descending();

        Sort sort1 = Sort.by("writer").descending();
        Sort sort2 = Sort.by("text").ascending();
        Sort sort = sort1.and(sort2); // 정렬 조건의 결합 and

        List<Memo> list = memoRepository.findAll(sort);

        System.out.println(list.toString());

    }

    // 페이지 클래스
    @Test
    public void testCode02() {

//        Pageable pageable = PageRequest.of(10, 10); // offset(페이지 번호), count(데이터 개수)
//        Pageable pageable = PageRequest.of(1, 10); // 11~20
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending()); // 11~20
        Page<Memo> page = memoRepository.findAll(pageable);
        // 페이지 타입 결과 안에는 데이터, 페이지에 대한 정보

        System.out.println("조회된 데이터:" + page.getContent());
        System.out.println("전체 페이지 수:" + page.getTotalPages());
        System.out.println("전체 데이터 수:" + page.getTotalElements());
        System.out.println("현재 페이지:" + page.getNumber());
        System.out.println("데이터 개수:" + page.getSize());
        System.out.println("이전 페이지 여부:" + page.hasPrevious());
        System.out.println("다음 페이지 여부:" + page.hasNext());
        System.out.println("현재 페이지가 마지막인지:" + page.isLast());
        System.out.println("현재 페이지가 처음인지:" + page.isFirst());
        // 등등

    }
}
