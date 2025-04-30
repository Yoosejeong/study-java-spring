package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);

        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); // 회원과 조인

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate orderStatus = cb.equal(o.get("orderStatus"), orderSearch.getOrderStatus());
            criteria.add(orderStatus);
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate memberName = cb.like(
                    m.get("name"),
                    "%" + orderSearch.getMemberName() + "%"
            );
            criteria.add(memberName);
        }

        // where 절 적용
        cq.where(cb.and(criteria.toArray(new Predicate[0])));

        // 최대 1000건으로 결과 제한
        return em.createQuery(cq)
                .setMaxResults(1000)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                     "select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.orderStatus, d.address) "
                             + "from Order o " +
                        "join o.member m " +
                        "join o.delivery d" , OrderSimpleQueryDto.class)
                .getResultList();

    }

}
