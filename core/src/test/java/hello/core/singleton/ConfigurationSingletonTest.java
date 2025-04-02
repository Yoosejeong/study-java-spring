package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest(){
       ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        
        System.out.println("memberService.getMemberRepository() = " + memberService.getMemberRepository());
        System.out.println("memberRepository2 = " + orderService.getMemberRepository());;

        Assertions.assertThat(memberService.getMemberRepository()).isSameAs(orderService.getMemberRepository());
        Assertions.assertThat(orderService.getMemberRepository()).isSameAs(memberService.getMemberRepository());
    }
}
