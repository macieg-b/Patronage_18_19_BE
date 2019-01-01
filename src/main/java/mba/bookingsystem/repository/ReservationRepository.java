package mba.bookingsystem.repository;

import mba.bookingsystem.model.db.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    Reservation findByUuid(UUID uuid);
}
