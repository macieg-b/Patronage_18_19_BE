package mba.bookingsystem.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mba.bookingsystem.model.PhoneInterface;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
public class Phone {
    @Column(nullable = true)
    private boolean phoneAvailable;
    @Column(nullable = true)
    private int extensionNumber;
    private String publicNumber;
    @Enumerated(value = EnumType.STRING)
    private PhoneInterface phoneInterface;

}
