package com.example.guitarshop.repository;

import com.example.guitarshop.domain.Guitar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Guitar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuitarRepository extends JpaRepository<Guitar, Long> {}
