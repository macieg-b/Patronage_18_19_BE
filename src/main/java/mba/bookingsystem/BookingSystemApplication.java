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

}

