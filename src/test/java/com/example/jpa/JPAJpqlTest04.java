package com.example.jpa;

import com.example.jpa.entity.Memo;
import com.example.jpa.memo.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class JPAJpqlTest04 {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testCode01() {
        List<Memo> list = memoRepository.getListDesc();

        System.out.println(list.toString());
    }

    @Test
    public void testcode02() {
        List<Memo> list = memoRepository.getListDesc2(50L);
        System.out.println(list.toString());
    }

    @Test
    public void testCode03() {
        List<Object[]> list = memoRepository.getListDesc3(50L);

        for(Object[] arr : list ) {
//            System.out.println(arr[0]);
//            System.out.println(arr[1]);
            System.out.println(Arrays.toString(arr));
        }

    }


    @Test
    public void testCode04() {

        int result = memoRepository.updateMemo("업데이트", 10L); //10번 데이터 업데이트
        System.out.println("성공실패:" + result);
    }

    @Test
    public void testCode05() {

        int result = memoRepository.updateMemo(
                Memo.builder()
                        .writer("업데이트")
                        .text("업데이트")
                        .mno(11L)
                        .build()
        );
        System.out.println("성공실패:" + result);


    }

    @Test
    public void testCode06() {
        memoRepository.deleteMemo(10L);
    }

    @Test
    public void testCode07() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.getListJPQL(100L, pageable);

        System.out.println("데이터:" +  result.getContent().toString() );
        System.out.println("전체게시글수:" + result.getTotalElements());
        System.out.println("현재페이지:" + result.getNumber());
    }

    @Test
    public void testCode08() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> list = memoRepository.getListJPQL2(100L, pageable);

        for(Object[] arr : list ) {
            System.out.println(Arrays.toString(arr));
        }


    }

    //네이티브 쿼리
    @Test
    public void testCode09() {
        Memo m = memoRepository.getNative(31L);
        System.out.println(m.toString());
    }




}
