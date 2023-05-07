package com.example.guitarshop.repository;

import com.example.guitarshop.domain.GuitarType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GuitarType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuitarTypeRepository extends JpaRepository<GuitarType, Long> {}
