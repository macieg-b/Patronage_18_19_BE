package mba.bookingsystem.model.view;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mba.bookingsystem.model.PhoneInterface;

import javax.validation.constraints.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PhoneView {
    @NotNull
    private Boolean phoneAvailable;
    @DecimalMin(value = "0")
    @DecimalMax(value = "99")
    private Integer extensionNumber;
    @Pattern(regexp = "\\+[0-9][0-9] [0-9]{9}")
    private String publicNumber;
    private PhoneInterface phoneInterface;

}
