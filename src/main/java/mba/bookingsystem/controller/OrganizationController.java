package mba.bookingsystem.controller;

import mba.bookingsystem.configuration.RestUrl;
import mba.bookingsystem.model.db.Organization;
import mba.bookingsystem.model.view.OrganizationView;
import mba.bookingsystem.service.OrganizationService;
import mba.bookingsystem.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static mba.bookingsystem.util.ModelMapper.convertToModel;
import static mba.bookingsystem.util.ModelMapper.convertToView;


@RestController
@RequestMapping(value = RestUrl.ORGANIZATION_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<List<OrganizationView>> getAll() {
        List<Organization> organizationList = organizationService.getAll();
        return ResponseEntity
                .ok(convertToView(organizationList, OrganizationView.class));
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<OrganizationView> getOne(@PathVariable UUID uuid) {
        Organization organization = organizationService.getOne(uuid);
        return ResponseEntity
                .ok(convertToView(organization, OrganizationView.class));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationView> create(@Valid @RequestBody OrganizationView organizationView) {
        Organization organization = organizationService.create(convertToModel(organizationView, Organization.class));
        return ResponseEntity
                .created(URI.create(
                        String.format("/%s/%s",
                                ServletUriComponentsBuilder.fromCurrentRequest().build().toUri(),
                                organization.getUuid())
                        )
                )
                .body(convertToView(organization, OrganizationView.class));
    }

    @PutMapping(value = "/{uuid}")
    public ResponseEntity<OrganizationView> update(@Valid @RequestBody OrganizationView organizationView, @PathVariable UUID uuid) {
        Organization organization = organizationService.update(convertToModel(organizationView, Organization.class), uuid);
        return ResponseEntity
                .ok(convertToView(organization, OrganizationView.class));

    }

    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        organizationService.delete(uuid);
        return ResponseEntity
                .ok()
                .build();

    }
}
