package jpabook.jpashop.domain;


import lombok.Data;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id") //pk 명시
    private Long id;

    private String name;

    @Embedded
    //여러 요소를 한 객체로 만들어서 가독성을 높이기 위해 간단히 표현해주는 어노테이션이다.
    private Address address;

    @OneToMany(mappedBy = "member") //member의 입장에서의 order는 한명의 회원이 여러개의 상품을 주문하기에, 일대다 관계가 형성. 따라서 oneToMany 사용. mappedBy는 양방향관계에서 거울을 의미
    private List  <Order> orders = new ArrayList<>(); //여기에 값을 넣는다고해서 fk값이 바뀌진 않는다.
    
}
