package mba.bookingsystem.validator;

import mba.bookingsystem.model.view.ReservationView;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AfterStartDateValidator implements ConstraintValidator<AfterStartDate, ReservationView> {
    @Override
    public void initialize(AfterStartDate constraintAnnotation) {

    }

    @Override
    public boolean isValid(ReservationView value, ConstraintValidatorContext context) {
        return value.getStartDate().before(value.getEndDate());
    }

}
