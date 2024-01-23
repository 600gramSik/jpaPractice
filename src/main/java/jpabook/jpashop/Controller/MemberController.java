package jpabook.jpashop.Controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) { //model은
        model.addAttribute("memberForm", new MemberForm()); //controller에서 view로 넘어갈때 memberform의 데이터를 실어서 넘긴다.
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) { //@Valid는 memberForm의 @NotEmpty가 붙은 요소를 validate해준다.

        if (result.hasErrors()) {
            return "members/createMemberForm";
        } //BindingResult로 에러를 잡아주고, 회원명을 작성하지 않을 시, 계속 createMemberform화면에 남도록 해준다.
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        memberService.join(member);
        return "redirect:/"; //첫번째 페이지로 넘겨준다

    }

    @GetMapping("/members")
    public String list(Model model) {
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }

    //주의사항: api를 만들때는 절대로 entity를 web으로 반환하면 안된다.

}
