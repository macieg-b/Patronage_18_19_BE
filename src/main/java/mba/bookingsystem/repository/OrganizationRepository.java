package mba.bookingsystem.repository;

import mba.bookingsystem.model.db.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    List<Organization> findAll();

    Organization findByUuid(UUID uuid);

    boolean existsByName(String name);

    Organization findByName(String name);
}
