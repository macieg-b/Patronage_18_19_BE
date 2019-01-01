package mba.bookingsystem.service;

import mba.bookingsystem.model.db.Organization;
import mba.bookingsystem.model.db.Reservation;
import mba.bookingsystem.repository.OrganizationRepository;
import mba.bookingsystem.repository.ReservationRepository;
import mba.bookingsystem.util.RepositoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, OrganizationRepository organizationRepository) {
        this.reservationRepository = reservationRepository;
        this.organizationRepository = organizationRepository;
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation getOne(UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Reservation.class, reservationRepository);
        return reservationRepository.findByUuid(uuid);
    }

    public Reservation create(Reservation reservation) {
        RepositoryValidator.ThrowNotFoundIfNotExist(reservation.getOrganizationUuid(), Organization.class, organizationRepository);
        reservation = reservationRepository.save(reservation);
        return reservation;
    }

    public Reservation update(Reservation reservation, UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Reservation.class, reservationRepository);
        reservation.setUuid(uuid);
        return reservationRepository.save(reservation);
    }

    public void delete(UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Reservation.class, reservationRepository);
        Reservation reservation = reservationRepository.findByUuid(uuid);
        reservationRepository.delete(reservation);
    }
}
