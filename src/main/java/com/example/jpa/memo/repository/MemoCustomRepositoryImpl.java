package com.example.jpa.memo.repository;

import com.example.jpa.entity.*;
import com.example.jpa.util.Criteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

public class MemoCustomRepositoryImpl implements MemoCustomRepository {
    //MemoRepository가 MemoCustomRepository를 상속받아줍니다.

    @PersistenceContext //엔티티 매니저를 주입을 받을 때 사용하는 어노테이션
    private EntityManager entityManager;

    ///////////////////쿼리DSL 사용시 엔티티매니저를 받아서 JPAFactory를 멤버변수에 저장/////////////
    private JPAQueryFactory jpaQueryFactory;

    public MemoCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager); //쿼리DSL팩토리는 생성될때 엔티티매니저를 받음.
    }


    @Override
    @Transactional //update구문이므로
    public int updateTest(String writer, long mno) {
        //기존에 인터페이스를 통해서 처리하던 JPQL구문을 직접 작성이 가능합니다.

        String sql = "update Memo m set m.writer = :a where m.mno = :b"; //JPQL

        Query query = entityManager.createQuery(sql);
        query.setParameter("a", writer); //a파라미터에 writer를 채움
        query.setParameter("b", mno);

        int result = query.executeUpdate(); //insert, update, delete구문을 실행할 때 사용

        return result;
    }

    @Override
    public List<Memo> mtoJoin1(long mno) {

        TypedQuery<Memo> result = entityManager.createQuery(
                "select m from Memo m inner join m.member where m.mno <= :mno", //조인의 테이블 위치에 엔티티의 필드명이 들어감
                //"select m from Memo m left join m.member where m.mno <= :mno",
                //"select m from Memo m right join m.member where m.mno <= :mno",
                Memo.class);

        result.setParameter("mno", mno); //파라미터 세팅
        List<Memo> list = result.getResultList(); //리스트 타입으로 반환받음

        return list;
    }

    @Override
    public List<Object[]> mtoJoin2(long mno) {

        TypedQuery<Object[]> result = entityManager.createQuery(
                "select m, x from Memo m left join m.member x where m.mno <= :mno"
                ,Object[].class
        );

        result.setParameter("mno", mno);
        List<Object[]> list = result.getResultList();

        return list;
    }

    @Override
    public List<Object[]> mtoJoin3(String writer) {

        TypedQuery<Object[]> result = entityManager.createQuery(
                "select m, x from Memo m inner join Member x on m.writer = x.name where m.writer = :writer"
                ,Object[].class
        );

        result.setParameter("writer", writer);
        List<Object[]> list = result.getResultList();

        return list;
    }

    @Override
    public List<Memo> mtoJoin4() {
        TypedQuery<Memo> result = entityManager.createQuery(
                "select m from Memo m left join fetch m.member x"
                ,Memo.class
        );
        return result.getResultList();
    }

    @Override
    public Member otmJoin1(String id) {

        TypedQuery<Member> result = entityManager.createQuery(
                "select m from Member m inner join m.list where m.id = :id"
                ,Member.class
        );
        result.setParameter("id", id);
        Member m = result.getSingleResult(); //1행인 경우
        return m;
    }

    @Override
    public List<Member> otmJoin2(String id) {

        TypedQuery<Member> result = entityManager.createQuery(
                //"select m from Member m inner join fetch m.list x where m.id = :id"
                "select distinct m from Member m inner join fetch m.list x where m.id = :id"
                ,Member.class
        );

        result.setParameter("id", id);
        List<Member> list = result.getResultList();

        return list;
    }

    @Override
    public List<MemberMemoDTO> otmJoin3(String id) {

        //구문의 select절에는 생성자의 맵핑하는 구문이 들어갑니다.
        TypedQuery<MemberMemoDTO> result = entityManager.createQuery(
                "select new com.example.jpa.entity.MemberMemoDTO(m.id, m.name, m.signDate, x.mno, x.writer, x.text ) " +
                        " from Member m inner join m.list x where m.id = :id"
                ,MemberMemoDTO.class
        );

        result.setParameter("id", id);
        List<MemberMemoDTO> list = result.getResultList();

        return list;
    }

    //SELECT M.id, M.name, M.signDate, X.mno, X.writer, X.text
    //FROM MEMO M
    //LEFT JOIN MEMBER X
    //ON M.MEMBER_ID = X.ID
    //WHERE M.TEXT LIKE '%1%';
//    @Override
//    public Page<MemberMemoDTO> joinPage(String text, Pageable pageable) {
//
//        TypedQuery<MemberMemoDTO> result = entityManager.createQuery(
//                "select new com.example.jpa.entity.MemberMemoDTO(x.id, x.name, x.signDate, m.mno, m.writer, m.text) " +
//                        "from Memo m left join m.member x where m.writer like :writer"
//                ,MemberMemoDTO.class
//        );
//
//        result.setParameter("writer", "%" + text + "%"); //파라미터 셋팅
//        result.setFirstResult(  (int)pageable.getOffset()  ); //페이지 시작 번호 - PageRequest.of(0, 10) <- 앞에값
//        result.setMaxResults( pageable.getPageSize() ); //데이터 개수 - PageRequest.of(0, 10) <- 뒤에값
//        List<MemberMemoDTO> list = result.getResultList(); //데이터
//
//        //countQuery를 실행
//        Query countQuery  = entityManager.createQuery(
//                "select count(m) from Memo m left join m.member x where m.writer like :writer"
//        );
//        countQuery.setParameter("writer", "%" + text + "%"); //파라미터 셋팅
//        Long count = (Long)countQuery.getSingleResult(); //한 행의 결과가 반환됨
//
//        //실행결과를 Pagealbe객체에 담는다.
//        PageImpl<MemberMemoDTO> page = new PageImpl<>(list, pageable, count); //데이터, 페이지객체, 토탈카운트
//
//        return page;
//    }


    //////////////////////////////////////////////////////////////////////////////////////////////////
    //쿼리DSL
    @Override
    public Memo dslSelect() {

        //엔티티를 대신해서 Q클래스를 사용함
        QMemo memo = QMemo.memo;

        //단일조회시 fetchOne, 여러행 조회 fetch(), insert, update, delete는 execute()
        //jpql - select m from Memo m where m.mno = 10
        Memo m = jpaQueryFactory.select(memo)
                .from(memo)
                .where(memo.mno.eq(11L) )
                .fetchOne();

        //select값을 선택적으로 받을 때는
//         Tuple t = jpaQueryFactory.select(memo.mno, memo.writer)
//                .from(memo)
//                .where(memo.mno.eq(11L) )
//                .fetchOne();
//
//        System.out.println( t.get(memo.mno) );
//        System.out.println( t.get(memo.writer) );
        return m;
    }
    @Override
    public List<Memo> dslSelect2() {

        QMemo memo =  QMemo.memo;
        List<Memo> list = jpaQueryFactory.select(memo)
                .from(memo)
                .where(memo.text.like("%2%")) //and, or등등..문서확인
                .orderBy(memo.text.asc() )
                .fetch(); //여러행 조회

        return list;
    }

    @Override
    public List<Memo> dslSelect3(String searchType, String searchName) {

        QMemo memo = QMemo.memo;

        //동적쿼리 구문을 만들때는 BooleanBuilder클래스를 사용합니다.
        BooleanBuilder builder = new BooleanBuilder();

        if(searchType.equals("writer")) { //searchType == writer
            builder.and( memo.writer.like( "%" + searchName + "%")  ); //조건절
        }

        if(searchType.equals("text")) { //searchType == text
            builder.and( memo.text.like( "%" + searchName + "%") );
        }

        List<Memo> list = jpaQueryFactory.select(memo)
                .from(memo)
                .where(builder)
                .fetch();

        return list;
    }

    @Override
    public List<Memo> dslJoin() {

        QMemo memo = QMemo.memo;
        QMember member = QMember.member;

        List<Memo> list = jpaQueryFactory.select(memo)
                .from(memo)
                //.join(memo.member , member ) //엔티티A.엔티티B = 엔티티B
                .leftJoin(memo.member, member)
                .fetch();

        return list;
    }

    @Override
    public Page<MemberMemoDTO> dslJoinPaging(Criteria cri, Pageable pageable) {

        QMemo memo = QMemo.memo;
        QMember member =QMember.member;
        //1. 불린빌더
        BooleanBuilder builder = new BooleanBuilder();

        if(cri.getSearchType().equals("mno") && !cri.getSearchName().isEmpty() ) { //searchType == xx 그리고 searchName != ""
            builder.and( memo.mno.eq(  Long.parseLong( cri.getSearchName() )  )); //where mno = x
        }

        if(cri.getSearchType().equals("text")) {
            builder.and( memo.text.like( "%" + cri.getSearchName()  + "%") ); //where text like %value%
        }

        if(cri.getSearchType().equals("writer")) {
            builder.and( memo.writer.like( "%" + cri.getSearchName()  + "%") ); //where text like %value%
        }

        if(cri.getSearchType().equals("textWriter")) {
            builder.and( memo.text.like( "%" + cri.getSearchName()  + "%") ); //where text like x or writer like x
            builder.or( memo.writer.like( "%" + cri.getSearchName() + "%") );
        }
        //2. join구문을 실행
//        "select new com.example.jpa.entity.MemberMemoDTO(x.id, x.name, x.signDate, m.mno, m.writer, m.text) " +
//        "from Memo m left join m.member x where 동적쿼리 limit 0, 10"
        List<MemberMemoDTO> list = jpaQueryFactory.select(
                        Projections.constructor(MemberMemoDTO.class,
                                member.id,
                                member.name,
                                member.signDate,
                                memo.mno,
                                memo.writer,
                                memo.text ) //타입, 생성자순서...
                )
                .from(memo)
                .leftJoin(memo.member, member)
                .where(builder) //불린빌더
                .orderBy( memo.mno.asc() )
                .offset( pageable.getOffset() ) //limit 0, 10 이중에 앞에 값을 의미함
                .limit( pageable.getPageSize() ) // limit 0, 10, 이중에 뒤에 값을 의미함
                .fetch();
        //3. countQuery실행
        long total = jpaQueryFactory
                .select(memo)
                .from(memo)
                .leftJoin(memo.member, member)
                .where(builder) //불린빌더
                .fetch().size();
        //4. Page객체에 맵핑
        PageImpl<MemberMemoDTO> page = new PageImpl<>(list, pageable, total); //데이터, 페이지객체, 전체게시글수

        return page;
    }
}
