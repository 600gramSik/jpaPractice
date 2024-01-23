package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional//데이터, 로직 변경이 일어나기에 꼭 추가해야됨

public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");
        //when
        Long savedId = memberService.join(member);

        //then
        Assert.assertEquals(member, memberRepository.findOne(savedId)); ;

    }
    
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_확인() throws Exception {
        //given(이렇게 주어졌을 때)
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when(이렇게 하면)
        memberService.join(member1);
        memberService.join(member2);
        
        //then(이렇게 된다!)
        fail("예외가 발생해야 한다."); //예외가 발생하지 않았다면 테스트가 실패한 것으로 간주된다.
    }

}