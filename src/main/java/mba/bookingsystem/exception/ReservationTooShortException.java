package mba.bookingsystem.exception;

public class ReservationTooShortException extends RuntimeException {
    public ReservationTooShortException(String message) {
        super(message);
    }
}
