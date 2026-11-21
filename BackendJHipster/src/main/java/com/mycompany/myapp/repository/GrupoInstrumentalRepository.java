package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.GrupoInstrumental;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GrupoInstrumental entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GrupoInstrumentalRepository extends JpaRepository<GrupoInstrumental, Long> {}
