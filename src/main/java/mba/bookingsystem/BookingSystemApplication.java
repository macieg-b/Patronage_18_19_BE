package mba.bookingsystem;

import mba.bookingsystem.model.db.Organization;
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

    private int ORGANIZATION_AMOUNT = 15;

    @Bean
    CommandLineRunner initInMemoryDatabase(OrganizationRepository organizationRepository) {
        return (args) -> IntStream.range(0, ORGANIZATION_AMOUNT)
                .forEach(i -> organizationRepository.save(new Organization(UUID.randomUUID(), String.format("Organization_%s", i))));
    }
}

