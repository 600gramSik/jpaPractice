package jpabook.jpashop.domain.Item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)//Inheritance는 상속관계 전략을 잡아준다(부모클래스에 추가)
@DiscriminatorColumn(name = "dtype")
public abstract class Item{ //구현체를 가지고 만들기 때문에 추상 클래스로!(Album, Book, Movie 들이 상속되어있다)
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //===비즈니스 로직===//
    //엔티티 자체에서 처리할 수 있는 로직이라면 엔티티에 로직을 추가하는 것이 좋다

    /**
     재고 증가
     */
    public void addStock(int stock) {
        this.stockQuantity +=stock;
    }
    /**
     재고 감소, 주문할 때 removeStock이 실행
     */
    public void removeStock(int stock) {
        int restStock = this.stockQuantity - stock;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
