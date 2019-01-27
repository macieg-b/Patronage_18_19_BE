package mba.bookingsystem.controller;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import mba.bookingsystem.configuration.RestUrl;
import mba.bookingsystem.model.PhoneInterface;
import mba.bookingsystem.model.db.Boardroom;
import mba.bookingsystem.model.db.Equipment;
import mba.bookingsystem.model.db.Phone;
import mba.bookingsystem.service.BoardroomService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
public class BoardroomControllerTest {
    private static final int BOARDROOM_LIST_SIZE = 10;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
    private static final UUID EXPECTED_UUID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Expected Name";
    private static final String EXPECTED_IDENTIFIER = "Expected identifier";
    private static final int EXPECTED_FLOOR = 1;
    private static final boolean EXPECTED_AVAILABILITY = true;
    private static final int EXPECTED_NORMAL_SEATS = 10;
    private static final int EXPECTED_LYING_SEATS = 10;
    private static final int EXPECTED_HANGING_SEATS = 2;
    private static final String EXPECTED_PROJECTOR_NAME = "Projector name";
    private static final boolean EXPECTED_PHONE_AVAILABILITY = true;
    private static final int EXPECTED_EXTENSION_PHONE = 101;
    private static final String EXPECTED_PHONE_INTERFACE = PhoneInterface.USB.toString();
    private static final String EXPECTED_PUBLIC_PHONE = "+12 123456789";
    private static final String BAD_PUBLIC_PHONE = "+12 12345678";

    @Mock
    private BoardroomService boardroomService;
    private MockMvc mockMvc;

    @Before
    public void prepare() {
        MockitoAnnotations.initMocks(this);
        final var boardroomController = new BoardroomController(boardroomService);
        mockMvc = MockMvcBuilders.standaloneSetup(boardroomController).build();
    }

    @DataProvider
    public static Object[] getCorrectBoardroom() {
        return new Boardroom[]{
                Boardroom.builder()
                        .uuid(EXPECTED_UUID)
                        .name(EXPECTED_NAME)
                        .identifier(EXPECTED_IDENTIFIER)
                        .floor(EXPECTED_FLOOR)
                        .available(EXPECTED_AVAILABILITY)
                        .normalSeats(EXPECTED_NORMAL_SEATS)
                        .lyingSeats(EXPECTED_LYING_SEATS)
                        .hangingSeats(EXPECTED_HANGING_SEATS)
                        .equipment(Equipment.builder()
                                .projectorName(EXPECTED_PROJECTOR_NAME)
                                .phone(Phone.builder()
                                        .phoneAvailable(EXPECTED_PHONE_AVAILABILITY)
                                        .extensionNumber(EXPECTED_EXTENSION_PHONE)
                                        .phoneInterface(PhoneInterface.valueOf(EXPECTED_PHONE_INTERFACE))
                                        .publicNumber(EXPECTED_PUBLIC_PHONE)
                                        .build())
                                .build())
                        .build()
        };
    }

    @DataProvider
    public static Object[] getBadPublicNumberBoardroom() {
        return new Boardroom[]{
                Boardroom.builder()
                        .uuid(EXPECTED_UUID)
                        .name(EXPECTED_NAME)
                        .identifier(EXPECTED_IDENTIFIER)
                        .floor(EXPECTED_FLOOR)
                        .available(EXPECTED_AVAILABILITY)
                        .normalSeats(EXPECTED_NORMAL_SEATS)
                        .lyingSeats(EXPECTED_LYING_SEATS)
                        .hangingSeats(EXPECTED_HANGING_SEATS)
                        .equipment(Equipment.builder()
                                .projectorName(EXPECTED_PROJECTOR_NAME)
                                .phone(Phone.builder()
                                        .phoneAvailable(EXPECTED_PHONE_AVAILABILITY)
                                        .extensionNumber(EXPECTED_EXTENSION_PHONE)
                                        .phoneInterface(PhoneInterface.valueOf(EXPECTED_PHONE_INTERFACE))
                                        .publicNumber(BAD_PUBLIC_PHONE)
                                        .build())
                                .build())
                        .build()
        };
    }

//    @DataProvider
//    public static Object[] getTooLongNameBoardroom() {
//        return new Boardroom[]{new Boardroom(EXPECTED_UUID, "Very long name up to 20 letters")};
//    }


    @Test
    public void getAllBoardroomSuccess() throws Exception {
        final var boardroomList = getBoardroomList(BOARDROOM_LIST_SIZE);
        when(boardroomService.getAll()).thenReturn(boardroomList);

        mockMvc.perform(get(RestUrl.BOARDROOM_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(BOARDROOM_LIST_SIZE)));
    }

    @Test
    @UseDataProvider("getCorrectBoardroom")
    public void getBoardroomByIdSuccess(final Boardroom boardroom) throws Exception {
        when(boardroomService.getOne(EXPECTED_UUID)).thenReturn(boardroom);
        mockMvc.perform(get(String.format("%s/%s", RestUrl.BOARDROOM_URL, EXPECTED_UUID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.uuid", Matchers.is(EXPECTED_UUID.toString())))
                .andExpect(jsonPath("$.name", Matchers.is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.identifier", Matchers.is(EXPECTED_IDENTIFIER)))
                .andExpect(jsonPath("$.floor", Matchers.is(EXPECTED_FLOOR)))
                .andExpect(jsonPath("$.available", Matchers.is(EXPECTED_AVAILABILITY)))
                .andExpect(jsonPath("$.normalSeats", Matchers.is(EXPECTED_NORMAL_SEATS)))
                .andExpect(jsonPath("$.lyingSeats", Matchers.is(EXPECTED_LYING_SEATS)))
                .andExpect(jsonPath("$.hangingSeats", Matchers.is(EXPECTED_HANGING_SEATS)))
                .andExpect(jsonPath("$.equipment.projectorName", Matchers.is(EXPECTED_PROJECTOR_NAME)))
                .andExpect(jsonPath("$.equipment.phone.phoneAvailable", Matchers.is(EXPECTED_PHONE_AVAILABILITY)))
                .andExpect(jsonPath("$.equipment.phone.extensionNumber", Matchers.is(EXPECTED_EXTENSION_PHONE)))
                .andExpect(jsonPath("$.equipment.phone.publicNumber", Matchers.is(EXPECTED_PUBLIC_PHONE)))
                .andExpect(jsonPath("$.equipment.phone.phoneInterface", Matchers.is(EXPECTED_PHONE_INTERFACE)));
    }

    @Test
    @UseDataProvider("getCorrectBoardroom")
    public void createBoardroomSuccess(final Boardroom boardroom) throws Exception {
        final var boardroomJson = modelToString(boardroom);

        when(boardroomService.create(any(Boardroom.class))).thenReturn(boardroom);
        mockMvc.perform(post(RestUrl.BOARDROOM_URL)
                .contentType(CONTENT_TYPE)
                .content(boardroomJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.identifier", Matchers.is(EXPECTED_IDENTIFIER)))
                .andExpect(jsonPath("$.floor", Matchers.is(EXPECTED_FLOOR)))
                .andExpect(jsonPath("$.available", Matchers.is(EXPECTED_AVAILABILITY)))
                .andExpect(jsonPath("$.normalSeats", Matchers.is(EXPECTED_NORMAL_SEATS)))
                .andExpect(jsonPath("$.lyingSeats", Matchers.is(EXPECTED_LYING_SEATS)))
                .andExpect(jsonPath("$.hangingSeats", Matchers.is(EXPECTED_HANGING_SEATS)))
                .andExpect(jsonPath("$.equipment.projectorName", Matchers.is(EXPECTED_PROJECTOR_NAME)))
                .andExpect(jsonPath("$.equipment.phone.phoneAvailable", Matchers.is(EXPECTED_PHONE_AVAILABILITY)))
                .andExpect(jsonPath("$.equipment.phone.extensionNumber", Matchers.is(EXPECTED_EXTENSION_PHONE)))
                .andExpect(jsonPath("$.equipment.phone.publicNumber", Matchers.is(EXPECTED_PUBLIC_PHONE)))
                .andExpect(jsonPath("$.equipment.phone.phoneInterface", Matchers.is(EXPECTED_PHONE_INTERFACE)));
    }

    @Test
    @UseDataProvider("getBadPublicNumberBoardroom")
    public void createBoardroomBadRequest(final Boardroom boardroom) throws Exception {
        final var boardroomJson = modelToString(boardroom);

        when(boardroomService.create(any(Boardroom.class))).thenReturn(boardroom);
        mockMvc.perform(post(RestUrl.BOARDROOM_URL)
                .contentType(CONTENT_TYPE)
                .content(boardroomJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteBoardroomById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(boardroomService).delete(id);
        mockMvc.perform(delete(String.format("%s/%s", RestUrl.BOARDROOM_URL, id)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @UseDataProvider("getCorrectBoardroom")
    public void updateBoardroom(final Boardroom boardroom) throws Exception {
        final var boardroomJson = modelToString(boardroom);

        when(boardroomService.update(any(Boardroom.class), eq(EXPECTED_UUID))).thenReturn(boardroom);
        mockMvc.perform(put(String.format("%s/%s", RestUrl.BOARDROOM_URL, EXPECTED_UUID))
                .contentType(CONTENT_TYPE)
                .content(boardroomJson))
                .andExpect(status().isOk());
    }

    private List<Boardroom> getBoardroomList(int count) {
        var boardroomList = new ArrayList<Boardroom>();
        IntStream.range(0, count)
                .forEach(
                        i -> boardroomList.add(Boardroom.builder()
                                .uuid(UUID.randomUUID())
                                .name(String.format("Boardroom_%s", i))
                                .identifier(String.format("%s.33", i))
                                .floor(i)
                                .available(i % 2 != 0)
                                .normalSeats(1000)
                                .lyingSeats(100)
                                .hangingSeats(5)
                                .equipment(Equipment.builder()
                                        .projectorName(String.format("Projector_%s", i))
                                        .phone(Phone.builder()
                                                .phoneAvailable(true)
                                                .extensionNumber(100 + i)
                                                .phoneInterface(i % 2 == 0 ? PhoneInterface.BLUETOOTH : PhoneInterface.USB)
                                                .build())
                                        .build())
                                .build())
                );
        return boardroomList;
    }

}