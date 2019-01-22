package mba.bookingsystem.model.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mba.bookingsystem.validator.AfterStartDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@AfterStartDate
public class ReservationView {
    private UUID uuid;
    @NotNull
    private OrganizationView organization;
    @NotNull
    private BoardroomView boardroom;
    @NotNull
    @FutureOrPresent
    private Date startDate;
    @NotNull
    @Future
    private Date endDate;
}
