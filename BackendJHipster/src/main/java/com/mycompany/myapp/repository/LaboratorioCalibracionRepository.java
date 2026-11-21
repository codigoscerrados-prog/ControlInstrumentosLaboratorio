package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LaboratorioCalibracion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LaboratorioCalibracion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LaboratorioCalibracionRepository extends JpaRepository<LaboratorioCalibracion, Long> {}
