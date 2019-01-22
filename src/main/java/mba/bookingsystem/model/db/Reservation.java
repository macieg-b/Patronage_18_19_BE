package mba.bookingsystem.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private UUID uuid;
    @ManyToOne
    private Organization organization;
    @ManyToOne
    private Boardroom boardroom;
    private Date startDate;
    private Date endDate;
}
