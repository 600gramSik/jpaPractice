package jpabook.jpashop.Controller;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

//근데 왜 member 엔티티도 있는데 굳이 memberForm을 따로 생성하나?
//entity자체를 화면에 왔다갔다하는 form데이터로 사용하게되면 딱 안맞게되고, 일이 더 늘어나서, 차라리 form데이터를 따로 생성해서 개발하는 것이 더 편하다.
@Data
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
