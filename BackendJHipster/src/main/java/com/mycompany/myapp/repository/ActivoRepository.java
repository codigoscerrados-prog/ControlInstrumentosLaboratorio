package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Activo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Activo entity.
 */
@Repository
public interface ActivoRepository extends JpaRepository<Activo, Long> {
    default Optional<Activo> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Activo> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Activo> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select activo from Activo activo left join fetch activo.grupo left join fetch activo.tipoInstrumento",
        countQuery = "select count(activo) from Activo activo"
    )
    Page<Activo> findAllWithToOneRelationships(Pageable pageable);

    @Query("select activo from Activo activo left join fetch activo.grupo left join fetch activo.tipoInstrumento")
    List<Activo> findAllWithToOneRelationships();

    @Query("select activo from Activo activo left join fetch activo.grupo left join fetch activo.tipoInstrumento where activo.id =:id")
    Optional<Activo> findOneWithToOneRelationships(@Param("id") Long id);
}
