package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository //componentScan에 의해 자동으로 spring bean으로 관리가 된다.
@RequiredArgsConstructor
public class MemberRepository {
    //@PersistenceContext //JPA를 사용하기에 표준 annotation이 필요!
    //최종적으로는 entityManager를 생성자로 주입하였다.
    private final EntityManager em;


    public void save(Member member) {
        em.persist(member);
    }
    public Member findOne(Long id) {
    //멤버 단건조회
        return em.find(Member.class, id);
    }

    public List<Member> findAll() { //jpql을 통해 데이터베이스와 직접적으로 상호작용을 이룬다.
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
