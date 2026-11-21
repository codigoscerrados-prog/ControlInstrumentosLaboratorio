package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Calibracion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Calibracion entity.
 */
@Repository
public interface CalibracionRepository extends JpaRepository<Calibracion, Long> {
    default Optional<Calibracion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Calibracion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Calibracion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select calibracion from Calibracion calibracion left join fetch calibracion.activo left join fetch calibracion.laboratorio",
        countQuery = "select count(calibracion) from Calibracion calibracion"
    )
    Page<Calibracion> findAllWithToOneRelationships(Pageable pageable);

    @Query("select calibracion from Calibracion calibracion left join fetch calibracion.activo left join fetch calibracion.laboratorio")
    List<Calibracion> findAllWithToOneRelationships();

    @Query(
        "select calibracion from Calibracion calibracion left join fetch calibracion.activo left join fetch calibracion.laboratorio where calibracion.id =:id"
    )
    Optional<Calibracion> findOneWithToOneRelationships(@Param("id") Long id);
}
