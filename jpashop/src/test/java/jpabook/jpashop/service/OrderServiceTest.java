package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //gieven
        Member member = createMember();

        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(getOrder.getOrderStatus())
                .as("상품 주문시 상태는 ORDER")
                .isEqualTo(OrderStatus.ORDER);
        assertThat(getOrder.getOrderItems().size())
                .as("주문한 상품 종류 수가 정확해야 한다.")
                .isEqualTo(1);
        assertThat(getOrder.getTotalPrice())
                .as("주문 가격은 가격 * 수량이다.")
                .isEqualTo(10000*orderCount);
        assertThat(book.getStockQuantity())
                .as("주문 수량만큼 재고가 줄어야 한다.")
                .isEqualTo(8);
    }


    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("시골 JPA", 100000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(getOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);
        int orderCount = 11;
        //when,then
        assertThrows(NotEnoughStockException.class,() -> orderService.order(member.getId(), book.getId(), orderCount));
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","김가","12-2"));
        em.persist(member);
        return member;
    }
}