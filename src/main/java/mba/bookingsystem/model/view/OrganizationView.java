package mba.bookingsystem.model.view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public class OrganizationView {

    private UUID uuid;
    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    public OrganizationView() {
    }

    public OrganizationView(UUID uuid, @NotNull @Size(min = 2, max = 20) String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }
}
