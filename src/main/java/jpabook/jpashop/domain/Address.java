package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable //member의 address위에 embedded 어노테이션이 붙어있다. 둘 중 하나만 해도 상관없음
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Address {
    private String city ;
    private String street;
    private String zipcode;

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
