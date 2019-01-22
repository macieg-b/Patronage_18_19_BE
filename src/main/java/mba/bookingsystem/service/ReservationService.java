package mba.bookingsystem.service;

import mba.bookingsystem.exception.BoardroomReservedException;
import mba.bookingsystem.exception.ReservationTooShortException;
import mba.bookingsystem.model.db.Boardroom;
import mba.bookingsystem.model.db.Organization;
import mba.bookingsystem.model.db.Reservation;
import mba.bookingsystem.repository.BoardroomRepository;
import mba.bookingsystem.repository.OrganizationRepository;
import mba.bookingsystem.repository.ReservationRepository;
import mba.bookingsystem.util.RepositoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.google.common.primitives.Longs.max;
import static com.google.common.primitives.Longs.min;

@Service
public class ReservationService {
    private final int MIN_RESERVATION_TIME = 5;
    private final ReservationRepository reservationRepository;
    private final OrganizationRepository organizationRepository;
    private final BoardroomRepository boardroomRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, OrganizationRepository organizationRepository, BoardroomRepository boardroomRepository) {
        this.reservationRepository = reservationRepository;
        this.organizationRepository = organizationRepository;
        this.boardroomRepository = boardroomRepository;
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation getOne(UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Reservation.class, reservationRepository);
        return reservationRepository.findByUuid(uuid);
    }

    public Reservation create(Reservation reservation) {
        validateReservation(reservation);
        return reservationRepository.save(reservation);
    }


    public Reservation update(Reservation reservation, UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Reservation.class, reservationRepository);
        validateReservation(reservation);
        throwIfNotAvailable(reservation);
        reservation.setUuid(uuid);
        return reservationRepository.save(reservation);
    }

    public void delete(UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Reservation.class, reservationRepository);
        var reservation = reservationRepository.findByUuid(uuid);
        reservationRepository.delete(reservation);
    }

    private void validateReservation(Reservation reservation) {
        RepositoryValidator.ThrowNotFoundIfNotExist(reservation.getOrganization().getUuid(), Organization.class, organizationRepository);
        RepositoryValidator.ThrowNotFoundIfNotExist(reservation.getBoardroom().getUuid(), Boardroom.class, boardroomRepository);
        throwIfShorterThanNMinutes(reservation, MIN_RESERVATION_TIME);
    }

    private void throwIfShorterThanNMinutes(Reservation reservation, int minReservationTime) throws BadRequest {
        if (Math.abs(reservation.getStartDate().getTime() - reservation.getEndDate().getTime()) < minReservationTime * 60000) {
            throw new ReservationTooShortException(String.format("Reservation cannot be shorter than %s minutes", minReservationTime));
        }
    }

    private void throwIfNotAvailable(Reservation reservation) {
        var boardroom = reservation.getBoardroom();
        List<Reservation> reservations = reservationRepository.findAllByBoardroomUuid(reservation.getBoardroom().getUuid());
        boolean overlap = reservations.stream()
                .anyMatch(res -> ifDatesOverlap(res.getStartDate(), res.getEndDate(), reservation.getStartDate(), reservation.getEndDate()));
        if (overlap) {
            throw new BoardroomReservedException(String.format("Boardroom %s had been reserved", boardroom.getUuid()));
        }
    }

    boolean ifDatesOverlap(Date startA, Date endA, Date startB, Date endB) {
        long overlap = max(0, min(endA.getTime(), endB.getTime()) - max(startA.getTime(), startB.getTime()));
        return overlap > 0;
    }
}
