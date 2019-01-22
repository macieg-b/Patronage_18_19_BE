package mba.bookingsystem.service;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import mba.bookingsystem.exception.NotFoundException;
import mba.bookingsystem.model.PhoneInterface;
import mba.bookingsystem.model.db.*;
import mba.bookingsystem.repository.BoardroomRepository;
import mba.bookingsystem.repository.OrganizationRepository;
import mba.bookingsystem.repository.ReservationRepository;
import org.h2.util.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class ReservationServiceTest {

    private final long ONE_MINUTE_IN_MILLIS = 60000;
    private static final UUID EXPECTED_UUID = UUID.randomUUID();
    private static final UUID EXPECTED_ORGANIZATION_UUID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Expected Name";
    private static final long EXPECTED_START = new Date(new java.util.Date().getTime() + 500000).getTime();
    private static final long EXPECTED_END = new Date(new java.util.Date().getTime() + 1000000).getTime();
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private BoardroomRepository boardroomRepository;

    private ReservationService reservationService;

    @Before
    public void prepare() {
        MockitoAnnotations.initMocks(this);
        reservationService = new ReservationService(reservationRepository, organizationRepository, boardroomRepository);
    }

    @DataProvider
    public static Object[] getCorrectReservation() {
        return new Reservation[]{
                Reservation.builder()
                        .uuid(EXPECTED_UUID)
                        .organization(Organization.builder()
                                .uuid(EXPECTED_ORGANIZATION_UUID)
                                .name("Name")
                                .build())
                        .boardroom(Boardroom.builder()
                                .uuid(UUID.randomUUID())
                                .name("Green")
                                .identifier("0.33")
                                .floor(0)
                                .available(false)
                                .normalSeats(1000)
                                .lyingSeats(100)
                                .hangingSeats(5)
                                .equipment(Equipment.builder()
                                        .projectorName("Projector 0")
                                        .phone(Phone.builder()
                                                .phoneAvailable(true)
                                                .extensionNumber(100)
                                                .publicNumber(null)
                                                .phoneInterface(PhoneInterface.BLUETOOTH)
                                                .build())
                                        .build())
                                .build())
                        .startDate(new Date(EXPECTED_START))
                        .endDate(new Date(EXPECTED_END))
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
        assertEquals(EXPECTED_ORGANIZATION_UUID, dbReservation.getOrganization().getUuid());
        assertEquals(EXPECTED_START, dbReservation.getStartDate().getTime());
        assertEquals(EXPECTED_END, dbReservation.getEndDate().getTime());
    }

    @Test
    @UseDataProvider("getCorrectReservation")
    public void createSuccess(final Reservation reservation) {
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(organizationRepository.existsById(any(UUID.class))).thenReturn(true);
        when(boardroomRepository.existsById(any(UUID.class))).thenReturn(true);
        when(organizationRepository.findByUuid(any(UUID.class))).thenReturn(getCorrectOrganization());
        Reservation dbReservation = reservationService.create(reservation);
        assertEquals(EXPECTED_UUID, dbReservation.getUuid());
        assertEquals(EXPECTED_ORGANIZATION_UUID, dbReservation.getOrganization().getUuid());
        assertEquals(EXPECTED_START, dbReservation.getStartDate().getTime());
        assertEquals(EXPECTED_END, dbReservation.getEndDate().getTime());
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
        when(organizationRepository.existsById(any(UUID.class))).thenReturn(true);
        when(boardroomRepository.existsById(any(UUID.class))).thenReturn(true);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation dbReservation = reservationService.update(reservation, reservationUuid);
        assertEquals(EXPECTED_UUID, dbReservation.getUuid());
        assertEquals(EXPECTED_ORGANIZATION_UUID, dbReservation.getOrganization().getUuid());
        assertEquals(EXPECTED_START, dbReservation.getStartDate().getTime());
        assertEquals(EXPECTED_END, dbReservation.getEndDate().getTime());
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

    @Test
    public void dateNotOverlap() {
        Date startA, endA, startB, endB;
        startA = new Date();
        endA = new Date(new Date().getTime() + (60 * ONE_MINUTE_IN_MILLIS));
        startB = new Date(new Date().getTime() + (80 * ONE_MINUTE_IN_MILLIS));
        endB = new Date(new Date().getTime() + (100 * ONE_MINUTE_IN_MILLIS));

        boolean overlap = reservationService.ifDatesOverlap(startA, endA, startB, endB);
        assertEquals(false, overlap);
    }

    @Test
    public void endBOverlapInAPeriod() {
        Date startA, endA, startB, endB;
        startA = new Date(new Date().getTime() + (60 * ONE_MINUTE_IN_MILLIS));
        endA = new Date(new Date().getTime() + (100 * ONE_MINUTE_IN_MILLIS));
        startB = new Date(new Date().getTime() + (20 * ONE_MINUTE_IN_MILLIS));
        endB = new Date(new Date().getTime() + (80 * ONE_MINUTE_IN_MILLIS));

        boolean overlap = reservationService.ifDatesOverlap(startA, endA, startB, endB);
        assertEquals(true, overlap);
    }

    @Test
    public void startBOverlapInAPeriod() {
        Date startA, endA, startB, endB;
        startA = new Date(new Date().getTime() + (60 * ONE_MINUTE_IN_MILLIS));
        endA = new Date(new Date().getTime() + (100 * ONE_MINUTE_IN_MILLIS));
        startB = new Date(new Date().getTime() + (80 * ONE_MINUTE_IN_MILLIS));
        endB = new Date(new Date().getTime() + (120 * ONE_MINUTE_IN_MILLIS));

        boolean overlap = reservationService.ifDatesOverlap(startA, endA, startB, endB);
        assertEquals(true, overlap);
    }

    @Test
    public void startBANDendBOverlapInAPeriod() {
        Date startA, endA, startB, endB;
        startA = new Date(new Date().getTime() + (60 * ONE_MINUTE_IN_MILLIS));
        endA = new Date(new Date().getTime() + (120 * ONE_MINUTE_IN_MILLIS));
        startB = new Date(new Date().getTime() + (80 * ONE_MINUTE_IN_MILLIS));
        endB = new Date(new Date().getTime() + (100 * ONE_MINUTE_IN_MILLIS));

        boolean overlap = reservationService.ifDatesOverlap(startA, endA, startB, endB);
        assertEquals(true, overlap);
    }
}