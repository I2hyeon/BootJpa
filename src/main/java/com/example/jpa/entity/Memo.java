package com.example.jpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity //jpa가 엔티티로 관리한다는 의미
@Table(name = "MEMO") //MEMO테이블
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Memo {

    //엔티티를 정의하면, 하이버네이트가 DDL구문을 대신 실행해주는데, spring.jpa.hibernate.ddl-auto=update 옵션

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment동작
//    오라클전략
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "이름")
//    @SequenceGenerator(name = "이름", sequenceName = "시퀀스명", initialValue = 1, allocationSize = 1)
    private long mno;
    @Column(length = 200, nullable = false)
    private String writer;
    @Column(columnDefinition = "varchar(200) default 'y' ") //만들고 싶은 제약을 직접 명시
    private String text;

    //N:1
    //FK 컬럼명을 명시하지 않으면 Member엔티티에 member_주키 로 자동 생성됩니다.
//    @ManyToOne(fetch = FetchType.LAZY) // manyToOne 기본값은 EAGER 방식
//    @JoinColumn(name = "member_id") //Member엔티티의 주키를 member_id컬럼에 저장하겠다(FK)
//    private Member member; //멤버 엔티티

    // 양방향 맵핑
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
