package com.example.guitarshop.repository;

import com.example.guitarshop.domain.GuitarOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface GuitarOrderRepositoryWithBagRelationships {
    Optional<GuitarOrder> fetchBagRelationships(Optional<GuitarOrder> guitarOrder);

    List<GuitarOrder> fetchBagRelationships(List<GuitarOrder> guitarOrders);

    Page<GuitarOrder> fetchBagRelationships(Page<GuitarOrder> guitarOrders);
}
