package com.example.jpa.util;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data //getter, setter
public class PageVO<T> {

    //화면에 그려질 pageNation을 계산하는 클래스
    private int start; //시작페이지 번호
    private int end; //마지막페이지 번호
    private boolean prev; //이전버튼
    private boolean next; //다음버튼

    private int page; //현재조회하는 페이지번호 <-- cri
    private int amount; //현재조회하는 데이터개수 <-- cri

    private long total; //전체 게시글 수
    private int realEnd; //맨 마지막페이지에 도달했을 때, 재 계산이 들어가는 실제 끝번호

    private List<Integer> pageList; //페이지 네이션 번호를 list로 생성

    private int pageSize = 5; //페이지네이션 크기

    //************************ 화면에서 처리할 데이터 ***********************
    private List<T> pageData;
    private int pageTotal; //전체 페이지 수

    //생성자 - JPA의 페이지 객체를 받습니다.
    public PageVO(Page<T> pageable) {
        this.page = pageable.getNumber() + 1; //1번 페이지가 0으로 들어오기 때문에 + 1
        this.amount = pageable.getSize(); //데이터 개수
        this.total = pageable.getTotalElements(); //전체게시글 수
        this.pageData = pageable.getContent(); //조회된 실제 데이터
        this.pageTotal = pageable.getTotalPages(); //전체 페이지 수

        //끝페이지 번호 계산
        //1~10번 페이지 조회시 -> 10
        //11~20번 페이지 조회시 -> 20
        //끝페이지 = 올림(현재조회하는 페이지 / 페이지네이션개수 ) * 페이지네이션개수
        this.end = (int)Math.ceil( this.page / (double)this.pageSize ) * this.pageSize;

        //시작페이지 번호 계산
        //시작페이지 = 끝번호 - 페이지네이션개수 + 1
        this.start = end - this.pageSize + 1;

        //실제 끝번호 재 계산
        //총 게시물 개수가 53개 -> 실제끝번호 6, end페이지 10
        //총 게시물 개수가 112개 -> 실제끝번호 12, end페이지 20
        //실제끝번호 = 올림(총게시물 수 / 조회하는 데이터개수)
        this.realEnd = (int)Math.ceil( this.total / (double)this.amount );

        //end 재 계산
        //112개 게시물 -> 1~10페이지번호를 볼때는, end=10, realEnd=12
        //           -> 11~20페이지번호를 볼때는, end=20, realEnd=12
        if (end > realEnd) {
            this.end = this.realEnd;
        }
        //이전버튼 활성화 여부
        //start값의 증가는 1, 11, 21, 31.......
        this.prev = this.start > 1;
        //다음버튼 활성화 여부
        this.next = this.realEnd > this.end;

        //페이지네이션 생성
        this.pageList = IntStream.rangeClosed(this.start, this.end).boxed().collect(Collectors.toList());

    }

}
