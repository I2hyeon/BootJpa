package com.example.jpa;


import com.example.jpa.entity.Memo;
import com.example.jpa.memo.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class JPABasicTest01 {


    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testCode01() {
        for(int i = 1; i <= 100; i++) {
            memoRepository.save(
                    Memo.builder()
                            .writer("example" + i)
                            .text("sample" + i)
                            .build()
            );
        }
    }


    // select - find가 붙은 메서드를 사용합니다.
    @Test
    public void testCode02() {
        Optional<Memo> result = memoRepository.findById(50L); // 50번 조회

        if(result.isPresent()) { // 값이 있으면 true
            Memo m = result.get();
            System.out.println(m.toString() );
        } else { // 값이 없으면
            System.out.println("조회할 값이 없음");
        }

    }

    // select - all
    @Test
    public void testCode03() {
        List<Memo> list = memoRepository.findAll();

        for(Memo m : list) {
            System.out.println(m.toString() );
        }
    }

    // update - save로 합니다
    @Test
    public void testCode04() {
        // 내부적으로 select로 번호를 확인하고 없으면 insert 있으면 update를 처리함
        Memo result = memoRepository.save (
                Memo.builder()
                        .mno(50L)
                        .writer("업데이트완!")
                        .text("update!")
                        .build()
        );
        System.out.println("업데이트 된 엔티티:" + result); // 업데이트 완료된 결과를 반환
    }

    // delete - 삭제는 delete or deleteByid
    @Test
    public void testCode05() {
        //
        memoRepository.deleteById(50L);
    }

}
