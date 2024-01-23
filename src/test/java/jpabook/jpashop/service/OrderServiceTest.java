package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Test

    public void 상품주문() throws Exception {
        //given(이렇게 주어졌을 때)
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);



        //when(이렇게 하면)
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then(이렇게 된다!)
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량 만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());




    }

    @Test
    public void 주문취소() throws Exception { //cmd+shift+t -> orderService로 이동
        //given(이렇게 주어졌을 때)
        Member member = createMember();
        Book item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when(이렇게 하면)
        orderService.cancelOrder(orderId);

        //then(이렇게 된다!)
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야한다.", 10, item.getStockQuantity());

    }
    @Test(expected = NotEnoughStockException.class) //재고 수량이 초과되면 옆의 exception이 터져야한다.
    public void 상품주문_재고수량초과() throws Exception {
        //given(이렇게 주어졌을 때)
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 11 ;



        //when(이렇게 하면)
        orderService.order(member.getId(), book.getId(), orderCount);
        
        //then(이렇게 된다!)
        fail("재고 수량 부족 예외가 발행해야 한다.");
        
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member); //엔티티를 db에 저장해줘야 id 값이 나온다(도메인에서 id가 generatedValue로 되어 있기에!)
        return member;
    }
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);//엔티티를 db에 저장해줘야 id 값이 나온다(도메인에서 id가 generatedValue로 되어 있기에!)
        return book;
    }

}