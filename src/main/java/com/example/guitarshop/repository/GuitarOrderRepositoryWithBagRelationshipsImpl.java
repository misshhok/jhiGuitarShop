package com.example.guitarshop.repository;

import com.example.guitarshop.domain.GuitarOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class GuitarOrderRepositoryWithBagRelationshipsImpl implements GuitarOrderRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<GuitarOrder> fetchBagRelationships(Optional<GuitarOrder> guitarOrder) {
        return guitarOrder.map(this::fetchGuitarsInOrders);
    }

    @Override
    public Page<GuitarOrder> fetchBagRelationships(Page<GuitarOrder> guitarOrders) {
        return new PageImpl<>(
            fetchBagRelationships(guitarOrders.getContent()),
            guitarOrders.getPageable(),
            guitarOrders.getTotalElements()
        );
    }

    @Override
    public List<GuitarOrder> fetchBagRelationships(List<GuitarOrder> guitarOrders) {
        return Optional.of(guitarOrders).map(this::fetchGuitarsInOrders).orElse(Collections.emptyList());
    }

    GuitarOrder fetchGuitarsInOrders(GuitarOrder result) {
        return entityManager
            .createQuery(
                "select guitarOrder from GuitarOrder guitarOrder left join fetch guitarOrder.guitarsInOrders where guitarOrder is :guitarOrder",
                GuitarOrder.class
            )
            .setParameter("guitarOrder", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<GuitarOrder> fetchGuitarsInOrders(List<GuitarOrder> guitarOrders) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, guitarOrders.size()).forEach(index -> order.put(guitarOrders.get(index).getId(), index));
        List<GuitarOrder> result = entityManager
            .createQuery(
                "select distinct guitarOrder from GuitarOrder guitarOrder left join fetch guitarOrder.guitarsInOrders where guitarOrder in :guitarOrders",
                GuitarOrder.class
            )
            .setParameter("guitarOrders", guitarOrders)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
