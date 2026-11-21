package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rol entity.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findOneByNombreIgnoreCase(String nombre);
}
