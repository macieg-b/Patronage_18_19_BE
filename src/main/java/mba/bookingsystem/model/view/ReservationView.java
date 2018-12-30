package mba.bookingsystem.model.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReservationView {
    private UUID uuid;
    @NotNull
    private UUID organizationUuid;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
}
