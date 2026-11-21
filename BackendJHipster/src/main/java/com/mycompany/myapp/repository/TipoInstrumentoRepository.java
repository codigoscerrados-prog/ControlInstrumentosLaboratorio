package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TipoInstrumento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoInstrumento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoInstrumentoRepository extends JpaRepository<TipoInstrumento, Long> {}
