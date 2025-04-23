package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        Member member = new Member();
        member.setName("김");

        Long savedId = memberService.join(member);
        Member findMember = memberService.findOne(savedId);

        Assertions.assertThat(member).isEqualTo(findMember);
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        Member member = new Member();
        member.setName("김");

        Member member2 = new Member();
        member2.setName("김");

        memberService.join(member);

        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }

}