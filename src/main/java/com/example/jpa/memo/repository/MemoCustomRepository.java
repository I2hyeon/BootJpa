package com.example.jpa.memo.repository;

import com.example.jpa.entity.Member;
import com.example.jpa.entity.MemberMemoDTO;
import com.example.jpa.entity.Memo;
import com.example.jpa.util.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemoCustomRepository {

    int updateTest(String writer, long mno);

    List<Memo> mtoJoin1(long mno); //manyToOne조인 (N + 1문제) - 조인을 하기 위해 추가적인 select구문을 날리는 행위
    List<Object[]> mtoJoin2(long mno); //두개 이상의 엔티티 동시에 조회 (반환 Object[])
    List<Object[]> mtoJoin3(String writer); //연관관계가 없는(FK제약 x) 엔티티 조인
    List<Memo> mtoJoin4(); //성능향상 Fetch조인 (로딩전략 보다 무조건 우선시 적용됨)

    //////////////////////////////////////////////////////
    //원투 매니 방식
    Member otmJoin1(String id); //OneToMany 조인
    List<Member> otmJoin2(String id); //Fetch Join - 1:N조인에서 Fetch방식은 중복행을 생성 (중복행 제거 disctinct)

    //////////////////////////////////////////////////////
    //다대일 양방향 맵핑 사용하세요~~

    List<MemberMemoDTO> otmJoin3(String id); //DTO로 반환받기
    //Page<MemberMemoDTO> joinPage(String text, Pageable pageable); //조인된 결과를 Pageable처리

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //쿼리DSL
    Memo dslSelect(); //쿼리dsl 단일행 조회
    List<Memo> dslSelect2(); //쿼리dsl 여러행 조회
    List<Memo> dslSelect3(String searchType, String searchName); //불린빌더를 사용한 동적쿼리
    List<Memo> dslJoin(); //쿼리dsl 조인
    Page<MemberMemoDTO> dslJoinPaging(Criteria cri, Pageable pageable); // 조인 + 동적쿼리 + 페이지

}
