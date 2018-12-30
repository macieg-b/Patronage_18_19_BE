package mba.bookingsystem.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    private UUID organizationUuid;
    private Date startDate;
    private Date endDate;
}
