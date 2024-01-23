package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jpabook.jpashop.domain.DeliveryStatus.COMP;

@Entity
@Table(name = "orders")
//이걸 안붙여주면 자동으로 Order라는 이름으로 테이블을 생성, 하지만 db명령어의 orderby와 겹치므로 오류가 날 수 있음 그래서 orders라고 이름을 따로 세팅해준다.
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue
    //@GeneratedValue는 식별자로 들어갈 값들을 자동으로 생성시켜준다.
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //order의 입장에선 member는 다대일 관계이기에 manyToOne 사용
    @JoinColumn(name = "member_id") //매핑은 member_id랑 해준다.
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // order와 orderItem을 매핑해준다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // orderItem과 delivery 뒤에 있는 cascadeType.ALL은 order를 persist할 경우, 얘네들도 자동을 persist를 날려준다.
    //하지만 어디까지 cascade를 적용해야하나? -> delivery, orderItem은 오직 order만 참조해서 쓴다. 다른 엔티티는 얘네를 참조하지 않는다.
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) // ORDER, CANCEL
    private OrderStatus status;

    //===생성 메서드===//
    //앞으로 생성쪽에서 변경사항이 일어나면 여기서만 바꾸면 끝!
    //주문 생성을 위한 복잡한 로직을 여기서 한번에 완성을 시켜주면 간단하다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //====양뱡향 연관관계 편의 메서드====
    //주인이 되는 객체에 추가해주면 된다.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==비즈니스 로직==//

    /**
     * 주문취소
     */
    public void cancel() {
        if (delivery.getStatus() == COMP)//이미 배송이 진행중이면, 주문 취소는 불가능!
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
    //==조회 로직==//

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
//        for(OrderItem orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return totalPrice;
    }
}
