package mba.bookingsystem.model.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardroomView {

    private UUID uuid;
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 20)
    private String name;
    @Size(min = 2, max = 20)
    private String identifier;
    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "10")
    private int floor;
    @NotNull
    private boolean available;
    @NotNull
    private int normalSeats;
    private int lyingSeats;
    private int hangingSeats;

    @Valid
    private EquipmentView equipment;
}
