package mba.bookingsystem;

import mba.bookingsystem.model.PhoneInterface;
import mba.bookingsystem.model.db.Boardroom;
import mba.bookingsystem.model.db.Equipment;
import mba.bookingsystem.model.db.Organization;
import mba.bookingsystem.model.db.Phone;
import mba.bookingsystem.repository.BoardroomRepository;
import mba.bookingsystem.repository.OrganizationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootApplication
public class BookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingSystemApplication.class, args);
    }

    private int ORGANIZATION_AMOUNT = 3;

    @Bean
    CommandLineRunner initInMemoryDatabase(OrganizationRepository organizationRepository, BoardroomRepository boardroomRepository) {
        return (args) -> IntStream.range(0, ORGANIZATION_AMOUNT)
                .forEach(i -> {
                    organizationRepository.save(new Organization(UUID.randomUUID(), String.format("Organization_%s", i)));
                    boardroomRepository.save(Boardroom.builder()
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
                            .build());
                });
    }
}

