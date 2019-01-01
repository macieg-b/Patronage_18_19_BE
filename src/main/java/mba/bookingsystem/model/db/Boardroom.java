package mba.bookingsystem.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Boardroom {
    @GeneratedValue
    @Id
    private UUID uuid;
    private String name;
    private String identifier;
    private int floor;
    private boolean available;
    private int normalSeats;
    private int lyingSeats;
    private int hangingSeats;
    @Column(nullable = true)
    private Equipment equipment;
}
