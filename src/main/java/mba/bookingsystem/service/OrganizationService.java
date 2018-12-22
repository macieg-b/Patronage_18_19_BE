package mba.bookingsystem.service;

import mba.bookingsystem.exception.AlreadyExistsException;
import mba.bookingsystem.model.db.Organization;
import mba.bookingsystem.repository.OrganizationRepository;
import mba.bookingsystem.util.RepositoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }

    public Organization getOne(UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Organization.class, organizationRepository);
        return organizationRepository.findByUuid(uuid);
    }

    public Organization create(Organization organization) {
        throwIfNameExists(organization.getName());
        organization.setUuid(UUID.randomUUID());
        organizationRepository.save(organization);
        return organization;
    }


    public Organization update(Organization organization, UUID uuid) {
        throwIfNameExists(organization.getName());
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Organization.class, organizationRepository);
        Organization dbOrganization = organizationRepository.findByUuid(uuid);
        dbOrganization.setName(organization.getName());
        organizationRepository.save(dbOrganization);
        return dbOrganization;
    }

    private void throwIfNameExists(String name) {
        if (organizationRepository.existsByName(name)) {
            throw new AlreadyExistsException(name);
        }
    }

    public void delete(UUID uuid) {
        RepositoryValidator.ThrowNotFoundIfNotExist(uuid, Organization.class, organizationRepository);
        Organization organization = organizationRepository.findByUuid(uuid);
        organizationRepository.delete(organization);
    }
}
