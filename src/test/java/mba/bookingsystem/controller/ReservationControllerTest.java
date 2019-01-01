package mba.bookingsystem.controller;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import mba.bookingsystem.configuration.RestUrl;
import mba.bookingsystem.model.db.Reservation;
import mba.bookingsystem.service.ReservationService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static mba.bookingsystem.util.ModelMapper.modelToString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(DataProviderRunner.class)
public class ReservationControllerTest {
    private static final int RESERVATION_LIST_SIZE = 10;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
    private static final UUID EXPECTED_UUID = UUID.randomUUID();
    private static final UUID EXPECTED_ORGANIZATION_UUID = UUID.randomUUID();
    private static final Long EXPECTED_START = new Date(new java.util.Date().getTime()).getTime();
    private static final Long EXPECTED_END = new Date(new java.util.Date().getTime()).getTime();


    @Mock
    private ReservationService reservationService;
    private MockMvc mockMvc;

    @Before
    public void prepare() {
        MockitoAnnotations.initMocks(this);
        final ReservationController reservationController = new ReservationController(reservationService);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @DataProvider
    public static Object[] getCorrectReservation() {
        return new Reservation[]{
                Reservation.builder()
                        .uuid(EXPECTED_UUID)
                        .organizationUuid(EXPECTED_ORGANIZATION_UUID)
                        .startDate(new Date(EXPECTED_START))
                        .endDate(new Date(EXPECTED_END))
                        .build()};
    }

    @DataProvider
    public static Object[] getBadReservation() {
        return new Reservation[]{
                Reservation.builder()
                        .uuid(EXPECTED_UUID)
                        .organizationUuid(EXPECTED_ORGANIZATION_UUID)
                        .startDate(new Date(EXPECTED_START))
                        .build()};
    }

    @Test
    public void getAllReservationSuccess() throws Exception {
        final List<Reservation> reservationList = getReservationList(RESERVATION_LIST_SIZE);
        when(reservationService.getAll()).thenReturn(reservationList);

        mockMvc.perform(get(RestUrl.RESERVATION_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(RESERVATION_LIST_SIZE)));
    }

    @Test
    @UseDataProvider("getCorrectReservation")
    public void getReservationByIdSuccess(final Reservation reservation) throws Exception {
        when(reservationService.getOne(EXPECTED_UUID)).thenReturn(reservation);
        mockMvc.perform(get(String.format("%s/%s", RestUrl.RESERVATION_URL, EXPECTED_UUID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.uuid", Matchers.is(EXPECTED_UUID.toString())));
    }

    @Test
    @UseDataProvider("getCorrectReservation")
    public void createReservationSuccess(final Reservation reservation) throws Exception {
        final String reservationJson = modelToString(reservation);

        when(reservationService.create(any(Reservation.class))).thenReturn(reservation);
        mockMvc.perform(post(RestUrl.RESERVATION_URL)
                .contentType(CONTENT_TYPE)
                .content(reservationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid", Matchers.is(EXPECTED_UUID.toString())))
                .andExpect(jsonPath("$.organizationUuid", Matchers.is(EXPECTED_ORGANIZATION_UUID.toString())))
                .andExpect(jsonPath("$.startDate", Matchers.is(EXPECTED_START)))
                .andExpect(jsonPath("$.endDate", Matchers.is(EXPECTED_END)));
    }

    @Test
    @UseDataProvider("getBadReservation")
    public void createReservationBadRequest(final Reservation reservation) throws Exception {
        final String reservationJson = modelToString(reservation);

        when(reservationService.create(any(Reservation.class))).thenReturn(reservation);
        mockMvc.perform(post(RestUrl.RESERVATION_URL)
                .contentType(CONTENT_TYPE)
                .content(reservationJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteReservationById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(reservationService).delete(id);
        mockMvc.perform(delete(String.format("%s/%s", RestUrl.RESERVATION_URL, id)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @UseDataProvider("getCorrectReservation")
    public void updateReservation(final Reservation reservation) throws Exception {
        final String reservationJson = modelToString(reservation);

        when(reservationService.update(any(Reservation.class), eq(EXPECTED_UUID))).thenReturn(reservation);
        mockMvc.perform(put(String.format("%s/%s", RestUrl.RESERVATION_URL, EXPECTED_UUID))
                .contentType(CONTENT_TYPE)
                .content(reservationJson))
                .andExpect(status().isOk());
    }

    private List<Reservation> getReservationList(int count) {
        List<Reservation> reservationList = new ArrayList<>();
        IntStream.range(0, count)
                .forEach(
                        i -> reservationList.add(Reservation
                                .builder()
                                .uuid(UUID.randomUUID())
                                .organizationUuid(UUID.randomUUID())
                                .startDate(new Date(new java.util.Date().getTime()))
                                .endDate(new Date(new java.util.Date().getTime()))
                                .build())
                );
        return reservationList;
    }

}