package jpabook.jpashop.domain;

import jpabook.jpashop.domain.Item.Item;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"), //현재의 엔티티를 참조하는 외래키
            inverseJoinColumns = @JoinColumn(name = "item_id") //연결된 엔티티를 참조하는 외래키
    )
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany
    private List<Category> child = new ArrayList<>();

    //연관관계 메서드
    //어떤 부모 카테고리(부모 엔티티)에 속한 자식 카테고리(자식 엔티티)를 추가하는데 사용되는 메서드이다.
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
