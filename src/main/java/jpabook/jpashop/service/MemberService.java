package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 얘도 자동으로 spring bean으로 등록이 된다.
@Transactional(readOnly = true) // jpa의 모든 데이터 변경이나 로직들은 transaction안에서 이루어져야한다.
//그리고 보통 읽기 메서드(ex.조회)에는 뒤에 readOnly = true를 붙여주고, 쓰기 메서드(ex. 회원가입)에는 붙이지 않는다.
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository; // RequiredArgsConstructor가 이의 생성자를 만들어줬다.

    //회원가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) { // findMembers가 비어있지 않으면
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}