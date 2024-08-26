package com.example.jpa.controller;

import com.example.jpa.entity.MemberMemoDTO;
import com.example.jpa.memo.repository.MemoRepository;
import com.example.jpa.util.Criteria;
import com.example.jpa.util.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JpaRestController {

    @Autowired
    MemoRepository memoRepository;

    @GetMapping("/getList")
    public PageVO<MemberMemoDTO> getList(Criteria cri) {

        //Criteria -> 페이지번호, 데이터개수, 검색키워드
        //PageDTO -> 페이지네이션계산
        //서비스 영역 생략............................
        Pageable pageable = PageRequest.of( cri.getPage() - 1 , cri.getAmount()); //0, 10으로 조회
        Page<MemberMemoDTO> page = memoRepository.joinPage( cri.getSearchName() , pageable); //JPA호출
        PageVO<MemberMemoDTO> pageVO = new PageVO<>(page);

        return pageVO;
    }

    @GetMapping("/getList")
    public PageVO<MemberMemoDTO> getList2(Criteria cri) {

        //Criteria -> 페이지번호, 데이터개수, 검색키워드
        //PageDTO -> 페이지네이션계산
        //서비스 영역 생략............................
        Pageable pageable = PageRequest.of( cri.getPage() - 1 , cri.getAmount()); //0, 10으로 조회
        Page<MemberMemoDTO> page = memoRepository.dslJoinPaging( cri, pageable); //JPA호출
        PageVO<MemberMemoDTO> pageVO = new PageVO<>(page);

        return pageVO;
    }

}
