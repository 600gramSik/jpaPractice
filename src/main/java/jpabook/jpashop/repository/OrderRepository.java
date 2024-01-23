package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;
    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

//    public List<Order> findAll(OrderSearch orderSearch) {
//        em.createQuery("select o from Order o join o.member m" +
//                " where o.status =:status" +
//                " and m.name like :name", Order.class)
//                //Order를 조회하고, member와 join을 해준다(join o.member m -> order와 연관된 member를 조인하라!)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                //setFirstResult(100)// 100부터
//                .setMaxResults(1000) //paging: 최대 1000건의 결과를 가져온다.
//                .getResultList();
//
    public List<Order> findAll(OrderSearch orderSearch) {
        //jpql로 직접 동적쿼리 해결(하지만 많이 번거롭고, 실수로 인한 버그가 충분히 발생할 수 있다)
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if(orderSearch.getOrderStatus()!= null) {
            if(isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status =: status";

        }
        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            if(isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like : name";

        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);
        if(orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())){
            query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }
    //하지만 QueryDsl로 해결하는 것이 가장 이상적!

}
