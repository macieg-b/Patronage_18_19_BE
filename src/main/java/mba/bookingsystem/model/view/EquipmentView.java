package mba.bookingsystem.model.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EquipmentView {

    private String projectorName;

    @NotNull
    @Valid
    private PhoneView phone;
}
