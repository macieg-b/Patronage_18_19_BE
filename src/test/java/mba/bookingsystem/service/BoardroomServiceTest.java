package mba.bookingsystem.service;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import lombok.Data;
import mba.bookingsystem.exception.AlreadyExistsException;
import mba.bookingsystem.exception.NotFoundException;
import mba.bookingsystem.model.PhoneInterface;
import mba.bookingsystem.model.db.Boardroom;
import mba.bookingsystem.model.db.Equipment;
import mba.bookingsystem.model.db.Phone;
import mba.bookingsystem.repository.BoardroomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class BoardroomServiceTest {

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

    @Mock
    private BoardroomRepository boardroomRepository;
    private BoardroomService boardroomService;

    @Before
    public void prepare() {
        MockitoAnnotations.initMocks(this);
        boardroomService = new BoardroomService(boardroomRepository);
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
    public Boardroom getCorrectBoardroom2() {
        return Boardroom.builder()
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
                .build();
    }


    @Test
    public void getAllSuccess() {
        when(boardroomRepository.findAll()).thenReturn(Collections.emptyList());
        var boardroomList = boardroomService.getAll();
        assertTrue(boardroomList.isEmpty());
    }

    @Test
    @UseDataProvider("getCorrectBoardroom")
    public void getOneSuccess(final Boardroom boardroom) {
        final var boardroomUuid = boardroom.getUuid();
        when(boardroomRepository.existsById(boardroomUuid)).thenReturn(true);
        when(boardroomRepository.findByUuid(boardroomUuid)).thenReturn(boardroom);

        var dbBoardroom = boardroomService.getOne(boardroomUuid);
        assertEquals(EXPECTED_UUID, dbBoardroom.getUuid());
        assertEquals(EXPECTED_NAME, dbBoardroom.getName());
        assertEquals(EXPECTED_IDENTIFIER, dbBoardroom.getIdentifier());
        assertEquals(EXPECTED_FLOOR, dbBoardroom.getFloor());
        assertEquals(EXPECTED_AVAILABILITY, dbBoardroom.isAvailable());
        assertEquals(EXPECTED_NORMAL_SEATS, dbBoardroom.getNormalSeats());
        assertEquals(EXPECTED_LYING_SEATS, dbBoardroom.getLyingSeats());
        assertEquals(EXPECTED_HANGING_SEATS, dbBoardroom.getHangingSeats());
        assertEquals(EXPECTED_PROJECTOR_NAME, dbBoardroom.getEquipment().getProjectorName());
        assertEquals(EXPECTED_PHONE_AVAILABILITY, dbBoardroom.getEquipment().getPhone().isPhoneAvailable());
        assertEquals(EXPECTED_EXTENSION_PHONE, dbBoardroom.getEquipment().getPhone().getExtensionNumber());
        assertEquals(EXPECTED_PUBLIC_PHONE, dbBoardroom.getEquipment().getPhone().getPublicNumber());
        assertEquals(EXPECTED_PHONE_INTERFACE, dbBoardroom.getEquipment().getPhone().getPhoneInterface().toString());
    }

    @Test
    @UseDataProvider("getCorrectBoardroom")
    public void createSuccess(final Boardroom boardroom) {
        when(boardroomRepository.save(boardroom)).thenReturn(boardroom);

        var dbBoardroom = boardroomService.create(boardroom);
        assertEquals(EXPECTED_UUID, dbBoardroom.getUuid());
        assertEquals(EXPECTED_NAME, dbBoardroom.getName());
        assertEquals(EXPECTED_IDENTIFIER, dbBoardroom.getIdentifier());
        assertEquals(EXPECTED_FLOOR, dbBoardroom.getFloor());
        assertEquals(EXPECTED_AVAILABILITY, dbBoardroom.isAvailable());
        assertEquals(EXPECTED_NORMAL_SEATS, dbBoardroom.getNormalSeats());
        assertEquals(EXPECTED_LYING_SEATS, dbBoardroom.getLyingSeats());
        assertEquals(EXPECTED_HANGING_SEATS, dbBoardroom.getHangingSeats());
        assertEquals(EXPECTED_PROJECTOR_NAME, dbBoardroom.getEquipment().getProjectorName());
        assertEquals(EXPECTED_PHONE_AVAILABILITY, dbBoardroom.getEquipment().getPhone().isPhoneAvailable());
        assertEquals(EXPECTED_EXTENSION_PHONE, dbBoardroom.getEquipment().getPhone().getExtensionNumber());
        assertEquals(EXPECTED_PUBLIC_PHONE, dbBoardroom.getEquipment().getPhone().getPublicNumber());
        assertEquals(EXPECTED_PHONE_INTERFACE, dbBoardroom.getEquipment().getPhone().getPhoneInterface().toString());
    }

    @Test(expected = AlreadyExistsException.class)
    @UseDataProvider("getCorrectBoardroom")
    public void createThrowAlreadyExists(final Boardroom boardroom) {
        when(boardroomRepository.save(boardroom)).thenReturn(boardroom);
        when(boardroomRepository.findByName(any(String.class))).thenReturn(
                Boardroom.builder()
                        .name(EXPECTED_NAME)
                        .uuid(UUID.randomUUID())
                        .build()
        );
        boardroomService.create(boardroom);
    }

    @Test
    @UseDataProvider("getCorrectBoardroom")
    public void updateSuccess(final Boardroom boardroom) {
        final var boardroomUuid = boardroom.getUuid();
        when(boardroomRepository.existsById(boardroomUuid)).thenReturn(true);
        when(boardroomRepository.findByUuid(boardroomUuid)).thenReturn(boardroom);
        when(boardroomRepository.save(any(Boardroom.class))).thenReturn(boardroom);

        Boardroom dbBoardroom = boardroomService.update(boardroom, boardroomUuid);
        assertEquals(EXPECTED_UUID, dbBoardroom.getUuid());
        assertEquals(EXPECTED_NAME, dbBoardroom.getName());
        assertEquals(EXPECTED_IDENTIFIER, dbBoardroom.getIdentifier());
        assertEquals(EXPECTED_FLOOR, dbBoardroom.getFloor());
        assertEquals(EXPECTED_AVAILABILITY, dbBoardroom.isAvailable());
        assertEquals(EXPECTED_NORMAL_SEATS, dbBoardroom.getNormalSeats());
        assertEquals(EXPECTED_LYING_SEATS, dbBoardroom.getLyingSeats());
        assertEquals(EXPECTED_HANGING_SEATS, dbBoardroom.getHangingSeats());
        assertEquals(EXPECTED_PROJECTOR_NAME, dbBoardroom.getEquipment().getProjectorName());
        assertEquals(EXPECTED_PHONE_AVAILABILITY, dbBoardroom.getEquipment().getPhone().isPhoneAvailable());
        assertEquals(EXPECTED_EXTENSION_PHONE, dbBoardroom.getEquipment().getPhone().getExtensionNumber());
        assertEquals(EXPECTED_PUBLIC_PHONE, dbBoardroom.getEquipment().getPhone().getPublicNumber());
        assertEquals(EXPECTED_PHONE_INTERFACE, dbBoardroom.getEquipment().getPhone().getPhoneInterface().toString());
    }

    @Test(expected = AlreadyExistsException.class)
    @UseDataProvider("getCorrectBoardroom")
    public void updateThrowAlreadyExists(final Boardroom boardroom) {
        when(boardroomRepository.save(boardroom)).thenReturn(boardroom);
        when(boardroomRepository.existsById(any(UUID.class))).thenReturn(true);
        when(boardroomRepository.existsByName(any(String.class))).thenReturn(true);
        when(boardroomRepository.findByUuid(any(UUID.class))).thenReturn(boardroom);
        when(boardroomRepository.findByName(any(String.class))).thenReturn(
                Boardroom.builder()
                        .name(EXPECTED_NAME)
                        .uuid(UUID.randomUUID())
                        .build()
        );
        boardroomService.update(boardroom, EXPECTED_UUID);
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getCorrectBoardroom")
    public void updateThrowNotFound(final Boardroom boardroom) {
        when(boardroomRepository.save(boardroom)).thenReturn(boardroom);
        when(boardroomRepository.existsById(any(UUID.class))).thenReturn(false);
        when(boardroomRepository.findByUuid(any(UUID.class))).thenReturn(boardroom);
        boardroomService.update(boardroom, EXPECTED_UUID);
    }

    @Test
    @UseDataProvider("getCorrectBoardroom")
    public void deleteSuccess(final Boardroom boardroom) {
        final var boardroomUuid = boardroom.getUuid();
        when(boardroomRepository.existsById(boardroomUuid)).thenReturn(true);
        boardroomService.delete(boardroomUuid);
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getCorrectBoardroom")
    public void deleteThrowNotFoundException(final Boardroom boardroom) {
        final var boardroomUuid = boardroom.getUuid();
        when(boardroomRepository.existsById(boardroomUuid)).thenReturn(false);
        boardroomService.delete(boardroomUuid);
    }
}