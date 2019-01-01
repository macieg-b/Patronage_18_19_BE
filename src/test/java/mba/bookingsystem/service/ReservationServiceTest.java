package mba.bookingsystem.service;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import mba.bookingsystem.exception.AlreadyExistsException;
import mba.bookingsystem.exception.NotFoundException;
import mba.bookingsystem.model.db.Organization;
import mba.bookingsystem.model.db.Reservation;
import mba.bookingsystem.repository.OrganizationRepository;
import mba.bookingsystem.repository.ReservationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class ReservationServiceTest {

    private static final UUID EXPECTED_UUID = UUID.randomUUID();
    private static final UUID EXPECTED_ORGANIZATION_UUID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Expected Name";
    private static final Date EXPECTED_START = new Date(new java.util.Date().getTime());
    private static final Date EXPECTED_END = new Date(new java.util.Date().getTime());

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    private ReservationService reservationService;

    @Before
    public void prepare() {
        MockitoAnnotations.initMocks(this);
        reservationService = new ReservationService(reservationRepository, organizationRepository);
    }

    @DataProvider
    public static Object[] getCorrectReservation() {
        return new Reservation[]{
                Reservation.builder()
                        .uuid(EXPECTED_UUID)
                        .organizationUuid(EXPECTED_ORGANIZATION_UUID)
                        .startDate(EXPECTED_START)
                        .endDate(EXPECTED_END)
                        .build()};
    }

    private Organization getCorrectOrganization() {
        return new Organization(EXPECTED_ORGANIZATION_UUID, EXPECTED_NAME);
    }

    @Test
    public void getAllSuccess() {
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());
        List<Reservation> reservationList = reservationService.getAll();
        assertTrue(reservationList.isEmpty());
    }

    @Test
    @UseDataProvider("getCorrectReservation")
    public void getOneSuccess(final Reservation reservation) {
        final UUID reservationUuid = reservation.getUuid();
        when(reservationRepository.existsById(reservationUuid)).thenReturn(true);
        when(reservationRepository.findByUuid(reservationUuid)).thenReturn(reservation);

        Reservation dbReservation = reservationService.getOne(reservationUuid);
        assertEquals(EXPECTED_UUID, dbReservation.getUuid());
        assertEquals(EXPECTED_ORGANIZATION_UUID, dbReservation.getOrganizationUuid());
        assertEquals(EXPECTED_START, dbReservation.getStartDate());
        assertEquals(EXPECTED_END, dbReservation.getEndDate());
    }

    @Test
    @UseDataProvider("getCorrectReservation")
    public void createSuccess(final Reservation reservation) {
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(organizationRepository.existsById(any(UUID.class))).thenReturn(true);
        when(organizationRepository.findByUuid(any(UUID.class))).thenReturn(getCorrectOrganization());
        Reservation dbReservation = reservationService.create(reservation);
        assertEquals(EXPECTED_UUID, dbReservation.getUuid());
        assertEquals(EXPECTED_ORGANIZATION_UUID, dbReservation.getOrganizationUuid());
        assertEquals(EXPECTED_START.getTime(), dbReservation.getStartDate().getTime());
        assertEquals(EXPECTED_END.getTime(), dbReservation.getEndDate().getTime());
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getCorrectReservation")
    public void createThrowNotFound(final Reservation reservation) {
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        reservationService.create(reservation);
    }

    @Test
    @UseDataProvider("getCorrectReservation")
    public void updateSuccess(final Reservation reservation) {
        final UUID reservationUuid = reservation.getUuid();
        when(reservationRepository.existsById(reservationUuid)).thenReturn(true);
        when(reservationRepository.findByUuid(reservationUuid)).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation dbReservation = reservationService.update(reservation, reservationUuid);
        assertEquals(EXPECTED_UUID, dbReservation.getUuid());
        assertEquals(EXPECTED_ORGANIZATION_UUID, dbReservation.getOrganizationUuid());
        assertEquals(EXPECTED_START.getTime(), dbReservation.getStartDate().getTime());
        assertEquals(EXPECTED_END.getTime(), dbReservation.getEndDate().getTime());
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getCorrectReservation")
    public void updateThrowNotFound(final Reservation reservation) {
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationRepository.existsById(any(UUID.class))).thenReturn(false);
        when(reservationRepository.findByUuid(any(UUID.class))).thenReturn(reservation);
        reservationService.update(reservation, EXPECTED_UUID);
    }

    @Test
    @UseDataProvider("getCorrectReservation")
    public void deleteSuccess(final Reservation reservation) {
        final UUID reservationUuid = reservation.getUuid();
        when(reservationRepository.existsById(reservationUuid)).thenReturn(true);
        reservationService.delete(reservationUuid);
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getCorrectReservation")
    public void deleteThrowNotFoundException(final Reservation reservation) {
        final UUID reservationUuid = reservation.getUuid();
        when(reservationRepository.existsById(reservationUuid)).thenReturn(false);
        reservationService.delete(reservationUuid);
    }
}