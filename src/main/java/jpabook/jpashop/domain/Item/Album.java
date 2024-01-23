package jpabook.jpashop.domain.Item;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue("A")
public class Album extends Item {
    private String artist;
    private String etc;
}
