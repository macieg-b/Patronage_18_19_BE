package mba.bookingsystem.repository;

import mba.bookingsystem.model.db.Boardroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardroomRepository extends JpaRepository<Boardroom, UUID> {

    List<Boardroom> findAll();

    Boardroom findByUuid(UUID uuid);

    boolean existsByName(String name);

    Boardroom findByName(String name);
}
