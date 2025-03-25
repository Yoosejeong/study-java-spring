package hello.hello_spring;

import hello.hello_spring.repository.JpaMemberRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.service.MemberService;
import jakarta.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private MemberRepository memberRepository;

    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //jpa
    //private EntityManager em;

    //@Autowired
    //public SpringConfig (EntityManager em){
    //    this.em = em;
    //}

    //@Bean
    //public MemberService memberService(){
    //    return new MemberService(memberRepository());
    //}

    //@Bean
    //public MemberRepository memberRepository(){
    //    return new JpaMemberRepository(em);
    //}

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository);
    }

}
