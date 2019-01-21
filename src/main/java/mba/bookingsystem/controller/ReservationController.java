package mba.bookingsystem.controller;

import mba.bookingsystem.configuration.RestUrl;
import mba.bookingsystem.model.db.Reservation;
import mba.bookingsystem.model.view.ReservationView;
import mba.bookingsystem.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static mba.bookingsystem.util.ModelMapper.convertToModel;
import static mba.bookingsystem.util.ModelMapper.convertToView;


@RestController
@RequestMapping(value = RestUrl.RESERVATION_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationView>> getAll() {
        var reservationList = reservationService.getAll();
        return ResponseEntity
                .ok(convertToView(reservationList, ReservationView.class));
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<ReservationView> getOne(@PathVariable UUID uuid) {
        var reservation = reservationService.getOne(uuid);
        return ResponseEntity
                .ok(convertToView(reservation, ReservationView.class));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationView> create(@Valid @RequestBody ReservationView reservationView) {
        var reservation = reservationService.create(convertToModel(reservationView, Reservation.class));
        return ResponseEntity
                .created(URI.create(
                        String.format("/%s/%s",
                                ServletUriComponentsBuilder.fromCurrentRequest().build().toUri(),
                                reservation.getUuid())
                        )
                )
                .body(convertToView(reservation, ReservationView.class));
    }

    @PutMapping(value = "/{uuid}")
    public ResponseEntity<ReservationView> update(@Valid @RequestBody ReservationView reservationView, @PathVariable UUID uuid) {
        var reservation = reservationService.update(convertToModel(reservationView, Reservation.class), uuid);
        return ResponseEntity
                .ok(convertToView(reservation, ReservationView.class));

    }

    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        reservationService.delete(uuid);
        return ResponseEntity
                .ok()
                .build();

    }
}
