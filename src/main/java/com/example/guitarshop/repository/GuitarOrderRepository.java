package com.example.guitarshop.repository;

import com.example.guitarshop.domain.GuitarOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GuitarOrder entity.
 *
 * When extending this class, extend GuitarOrderRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface GuitarOrderRepository extends GuitarOrderRepositoryWithBagRelationships, JpaRepository<GuitarOrder, Long> {
    @Query("select guitarOrder from GuitarOrder guitarOrder where guitarOrder.orderOwner.login = ?#{principal.username}")
    List<GuitarOrder> findByOrderOwnerIsCurrentUser();

    default Optional<GuitarOrder> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<GuitarOrder> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<GuitarOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct guitarOrder from GuitarOrder guitarOrder left join fetch guitarOrder.orderOwner",
        countQuery = "select count(distinct guitarOrder) from GuitarOrder guitarOrder"
    )
    Page<GuitarOrder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct guitarOrder from GuitarOrder guitarOrder left join fetch guitarOrder.orderOwner")
    List<GuitarOrder> findAllWithToOneRelationships();

    @Query("select guitarOrder from GuitarOrder guitarOrder left join fetch guitarOrder.orderOwner where guitarOrder.id =:id")
    Optional<GuitarOrder> findOneWithToOneRelationships(@Param("id") Long id);
}
