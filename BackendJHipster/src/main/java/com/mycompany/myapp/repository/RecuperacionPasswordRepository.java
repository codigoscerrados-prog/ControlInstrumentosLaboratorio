package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RecuperacionPassword;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecuperacionPassword entity.
 */
@Repository
public interface RecuperacionPasswordRepository extends JpaRepository<RecuperacionPassword, Long> {
    default Optional<RecuperacionPassword> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RecuperacionPassword> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RecuperacionPassword> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select recuperacionPassword from RecuperacionPassword recuperacionPassword left join fetch recuperacionPassword.usuario",
        countQuery = "select count(recuperacionPassword) from RecuperacionPassword recuperacionPassword"
    )
    Page<RecuperacionPassword> findAllWithToOneRelationships(Pageable pageable);

    @Query("select recuperacionPassword from RecuperacionPassword recuperacionPassword left join fetch recuperacionPassword.usuario")
    List<RecuperacionPassword> findAllWithToOneRelationships();

    @Query(
        "select recuperacionPassword from RecuperacionPassword recuperacionPassword left join fetch recuperacionPassword.usuario where recuperacionPassword.id =:id"
    )
    Optional<RecuperacionPassword> findOneWithToOneRelationships(@Param("id") Long id);
}
