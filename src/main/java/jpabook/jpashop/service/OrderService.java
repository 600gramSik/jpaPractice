package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //jpashop 인터페이스에서 주문회원: memberId, 상품명: itemId, 주문수량: count

        //엔티티 조회
        //트랜젝션 안에서 객체 생성 후 작업을 하면, 영속성을 그대로 유지해준다.
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 조회
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        /**만약 orderItem이나 order를 다른 방법으로 생성하게 되면?
         ex) Order order = new Order();
         order.setDelivery(delivery) ... 요런식으로?
         그럼 코드의 유지보수가 어렵고 복잡하게 된다. 이를 방지하려면?
         해당 entity로 가서 protected 생성자를 생성한다! or @NoArgsConsctructor(access = AccessLevel.PROTECTED)
         **/

        //주문 저장
        orderRepository.save(order);
        return order.getId();

    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) { //주문 취소할 때 id값만 넘어오게!
        Order order = orderRepository.findOne(orderId); //주문 엔티티 조회
        order.cancel(); //주문 취소

    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

}
