package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RateDiscountPolicyTest {
    RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.")
    void vip_o() {
        Member member = new Member(1L, "memberA", Grade.VIP);
        int discount = rateDiscountPolicy.discount(member, 20000);
        assertThat(discount).isEqualTo(2000);
    }

    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다.")
    void vip_x(){
        Member member = new Member(2L, "memberB", Grade.BASIC);
        int discount = rateDiscountPolicy.discount(member, 20000);
        assertThat(discount).isEqualTo(0);
    }
}