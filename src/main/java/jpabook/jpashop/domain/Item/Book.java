package jpabook.jpashop.domain.Item;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue("B")
public class Book extends Item {
    private String author;
    private String isbn;
}
