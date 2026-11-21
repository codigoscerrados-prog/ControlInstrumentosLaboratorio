package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cursos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cursos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CursosRepository extends JpaRepository<Cursos, Long> {}
