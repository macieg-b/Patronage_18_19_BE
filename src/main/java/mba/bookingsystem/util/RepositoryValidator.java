package mba.bookingsystem.util;

import mba.bookingsystem.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public class RepositoryValidator {

    public static void ThrowNotFoundIfNotExist(UUID uuid, Class cls, JpaRepository repository) {
        if (!repository.existsById(uuid)) {
            throw new NotFoundException(cls.getSimpleName());
        }
    }
}
