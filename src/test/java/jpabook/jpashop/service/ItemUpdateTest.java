package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemUpdateTest {
    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception {
        Book book = em.find(Book.class, "1L");
        book.setName("asdfasdf");
    }
    //변경감지(Dirty Checking)이란? -> jpa에서 요소 변경의 기본적인 로직
    //Transaction안에서는 book.setName으로 이름을 변경하고,
    //transacgtion이 커밋되는 순간, jpa가 이를 자동으로 찾아, updateQuery를 생성해서 db에 반영을 해주는 것.
}
